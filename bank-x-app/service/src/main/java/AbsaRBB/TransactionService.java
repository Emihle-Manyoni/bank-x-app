package AbsaRBB;

import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.ReconciliationEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountsRepository accountsRepository;
    private final ExternalBankRepository externalBankRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AccountsDomainRepo accountsDomainRepo;
    private final ReconciliationRepository reconciliationRepository;

    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              AccountsRepository accountsRepository,
                              ExternalBankRepository externalBankRepository, EntityDTOMapper entityDTOMapper,
                              AccountsDomainRepo accountsDomainRepo, ReconciliationRepository reconciliationRepository) {
        this.transactionRepository = transactionRepository;
        this.accountsRepository = accountsRepository;
        this.externalBankRepository = externalBankRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.accountsDomainRepo = accountsDomainRepo;
        this.reconciliationRepository = reconciliationRepository;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(entityDTOMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
        return entityDTOMapper.toTransactionDTO(transaction);
    }

    @Transactional
    public TransactionDTO createPayment(TransactionDTO transactionDTO) {
        AccountsEntity account = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + transactionDTO.getAccountID()));
        if (!account.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
            throw new RuntimeException("Customer verification failed. Account does not belong to the specified customer.");
        }

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        transactionDTO.setCreatedDate(new Date());
        if (transactionDTO.getTransactionDate() == null) {
            transactionDTO.setTransactionDate(new Date());
        }

        if (transactionDTO.getTransactionType() == null || transactionDTO.getTransactionType().isEmpty()) {
            transactionDTO.setTransactionType("Payment");
        }

        ExternalBankEntity externalBank = null;
        if (transactionDTO.getExternalBankID() != null) {
            externalBank = externalBankRepository.findById(transactionDTO.getExternalBankID())
                    .orElse(null);
            transactionDTO.setExternal(true);
        } else {
            transactionDTO.setExternal(false);
        }

        double newBalance = account.getBalance() - transactionDTO.getTransactionAmount() - transactionFee;

        if (newBalance < 0) {
            throw new RuntimeException("Insufficient funds for this transaction");
        }

        account.setBalance(newBalance);
        accountsRepository.save(account);

        TransactionEntity transactionEntity = entityDTOMapper.toTransactionEntity(transactionDTO, account, externalBank);
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);


        System.out.println("Notification Service Payment successful: R" + transactionDTO.getTransactionAmount() +
                " from account " + account.getAccountNumber());

        return entityDTOMapper.toTransactionDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO createInternalTransfer(TransactionDTO transactionDTO) {
        AccountsEntity sourceAccount = accountsRepository.findById(transactionDTO.getSourceAccountID())
                .orElseThrow(() -> new RuntimeException("Source account not found with ID: " + transactionDTO.getSourceAccountID()));

        AccountsEntity destinationAccount = accountsRepository.findById(transactionDTO.getDestinationAccountID())
                .orElseThrow(() -> new RuntimeException("Destination account not found with ID: " + transactionDTO.getDestinationAccountID()));

        if (!sourceAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID()) ||
                !destinationAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
            throw new RuntimeException("Customer verification failed. One or both accounts do not belong to the specified customer.");
        }

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        double totalDeduction = transactionDTO.getTransactionAmount() + transactionFee;
        if (sourceAccount.getBalance() < totalDeduction) {
            throw new RuntimeException("Insufficient funds in source account");
        }

        sourceAccount.setBalance(sourceAccount.getBalance() - totalDeduction);
        destinationAccount.setBalance(destinationAccount.getBalance() + transactionDTO.getTransactionAmount());

        accountsRepository.save(sourceAccount);
        accountsRepository.save(destinationAccount);

        TransactionEntity sourceTransaction = new TransactionEntity();
        sourceTransaction.setAccount(sourceAccount);
        sourceTransaction.setTransactionAmount((int) transactionDTO.getTransactionAmount());
        sourceTransaction.setTransactionType("Transfer Out");
        sourceTransaction.setTransactionDate(new Date());
        sourceTransaction.setCreatedDate(new Date());
        sourceTransaction.setExternal(false);
        sourceTransaction.setTransactionCharge(transactionFee);
        transactionRepository.save(sourceTransaction);

        TransactionEntity destTransaction = new TransactionEntity();
        destTransaction.setAccount(destinationAccount);
        destTransaction.setTransactionAmount((int) transactionDTO.getTransactionAmount());
        destTransaction.setTransactionType("Transfer In");
        destTransaction.setTransactionDate(new Date());
        destTransaction.setCreatedDate(new Date());
        destTransaction.setExternal(false);
        destTransaction.setTransactionCharge(0);
        transactionRepository.save(destTransaction);

        return entityDTOMapper.toTransactionDTO(sourceTransaction);
    }

    @Transactional
    public TransactionDTO createExternalTransaction(TransactionDTO transactionDTO) {

        AccountsEntity account = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account not found with ID: " + transactionDTO.getAccountID()));

        ExternalBankEntity externalBank = externalBankRepository.findById(transactionDTO.getExternalBankID())
                .orElseThrow(() -> new RuntimeException("External bank not found with ID: " + transactionDTO.getExternalBankID()));

        transactionDTO.setExternal(true);
        transactionDTO.setCreatedDate(new Date());
        if (transactionDTO.getTransactionDate() == null) {
            transactionDTO.setTransactionDate(new Date());
        }

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        if ("Credit".equalsIgnoreCase(transactionDTO.getTransactionType())) {
            account.setBalance(account.getBalance() + transactionDTO.getTransactionAmount() - transactionFee);
        } else if ("Debit".equalsIgnoreCase(transactionDTO.getTransactionType())) {
            double newBalance = account.getBalance() - transactionDTO.getTransactionAmount() - transactionFee;
            if (newBalance < 0) {
                throw new RuntimeException("Insufficient funds for this transaction");
            }
            account.setBalance(newBalance);
        } else {
            throw new RuntimeException("Invalid transaction type. Must be 'Credit' or 'Debit'");
        }

        accountsRepository.save(account);

        TransactionEntity transactionEntity = entityDTOMapper.toTransactionEntity(transactionDTO, account, externalBank);
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        System.out.println("Transaction saved successfully with ID: " + savedTransaction.getTransactionID());

        ReconciliationEntity reconciliation = new ReconciliationEntity();
        reconciliation.setTransactionAmount(transactionDTO.getTransactionAmount());
        reconciliation.setTransactionDate(transactionDTO.getTransactionDate());
        reconciliation.setTransactionCharge(transactionFee);
        reconciliation.setCreatedDate(new Date());
        reconciliation.setTransactionType(transactionDTO.getTransactionType());
        reconciliation.setStatus("Pending");
        reconciliation.setReconciliationStatus("Unmatched");
        reconciliation.setBankName(externalBank.getBankName());
        reconciliation.setBankCode(externalBank.getBankCode());


        return entityDTOMapper.toTransactionDTO(savedTransaction);
    }
}
