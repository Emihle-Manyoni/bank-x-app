package AbsaRBB;

import AbsaRBB.Exceptions.*;
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
public class OffUsTransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountsRepository accountsRepository;
    private final ExternalBankRepository externalBankRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AccountsDomainRepo accountsDomainRepo;
    private final ReconciliationRepository reconciliationRepository;

    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Autowired
    public OffUsTransactionService(TransactionRepository transactionRepository,
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

    @Transactional
    public TransactionDTO createExternalTransaction(TransactionDTO transactionDTO) {

        AccountsEntity account = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new AccountNotFoundException( transactionDTO.getAccountID()));

        ExternalBankEntity externalBank = externalBankRepository.findById(transactionDTO.getExternalBankID())
                .orElseThrow(() -> new ExternalBankAccountNotFoundException(transactionDTO.getExternalBankID()));

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

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
