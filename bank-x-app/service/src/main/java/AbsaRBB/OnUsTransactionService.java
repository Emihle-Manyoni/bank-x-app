package AbsaRBB;

import AbsaRBB.Exceptions.AccountNotFoundException;
import AbsaRBB.Exceptions.InsufficientFundsException;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OnUsTransactionService {

    private final AccountsRepository accountsRepository;
    private final TransactionRepository transactionRepository;
    private final EntityDTOMapper entityDTOMapper;


    @Autowired
    public OnUsTransactionService(AccountsRepository accountsRepository, TransactionRepository transactionRepository, EntityDTOMapper entityDTOMapper) {
        this.accountsRepository = accountsRepository;
        this.transactionRepository = transactionRepository;
        this.entityDTOMapper = entityDTOMapper;
    }

    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Transactional
    public TransactionDTO createPayment(TransactionDTO transactionDTO) {
        AccountsEntity account = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new AccountNotFoundException(transactionDTO.getAccountID()));

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        ExternalBankEntity externalBank = null;

        double newBalance = account.getBalance() - transactionDTO.getTransactionAmount() - transactionFee;

        if (newBalance < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(newBalance);
        accountsRepository.save(account);

        TransactionEntity transactionEntity = entityDTOMapper.toTransactionEntity(transactionDTO, account, externalBank);
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        System.out.println("Notification Service Payment successful: R" + transactionDTO.getTransactionAmount() +
                " from account " + account.getAccountNumber());

        return entityDTOMapper.toTransactionDTO(savedTransaction);
    }
}
