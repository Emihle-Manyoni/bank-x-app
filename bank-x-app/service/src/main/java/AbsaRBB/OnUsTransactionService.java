package AbsaRBB;

import AbsaRBB.Exceptions.AccountNotFoundException;
import AbsaRBB.Exceptions.BadRequestException;
import AbsaRBB.Exceptions.InsufficientFundsException;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
        AccountsEntity onUsSourceAccount = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new AccountNotFoundException(transactionDTO.getAccountID()));

        AccountsEntity onUsDestinationAccount = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new AccountNotFoundException(transactionDTO.getAccountID()));

        if (!onUsSourceAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID()) ||
                !onUsDestinationAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
            throw new BadRequestException("Customer verification failed. One or both accounts do not belong to the customer the sender and reciever.");
        }



        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        ExternalBankEntity externalBank = null;

        double totalDeduction = transactionDTO.getTransactionAmount() + transactionFee;
        if (onUsSourceAccount.getBalance() < totalDeduction) {
            throw new InsufficientFundsException();
        }

        onUsSourceAccount.setBalance(onUsSourceAccount.getBalance() - totalDeduction);
        onUsDestinationAccount.setBalance(onUsDestinationAccount.getBalance() + transactionDTO.getTransactionAmount());

        accountsRepository.save(onUsSourceAccount);
        accountsRepository.save(onUsDestinationAccount);

        TransactionEntity sourceTransaction = new TransactionEntity();
        sourceTransaction.setAccount(onUsSourceAccount);
        sourceTransaction.setTransactionAmount((int) transactionDTO.getTransactionAmount());
        sourceTransaction.setTransactionType("Transfer Out");
        sourceTransaction.setTransactionDate(new Date());
        sourceTransaction.setCreatedDate(new Date());
        sourceTransaction.setExternal(false);
        sourceTransaction.setTransactionCharge(transactionFee);
        transactionRepository.save(sourceTransaction);

        TransactionEntity destTransaction = new TransactionEntity();
        destTransaction.setAccount(onUsDestinationAccount);
        destTransaction.setTransactionAmount((int) transactionDTO.getTransactionAmount());
        destTransaction.setTransactionType("Transfer In");
        destTransaction.setTransactionDate(new Date());
        destTransaction.setCreatedDate(new Date());
        destTransaction.setExternal(false);
        destTransaction.setTransactionCharge(0);
        transactionRepository.save(destTransaction);

        System.out.println("Notification Service Payment successful: R" + transactionDTO.getTransactionAmount() +
                " from account " + onUsSourceAccount.getAccountNumber());


        System.out.println("Notification Money Recieved successful: R" + transactionDTO.getTransactionAmount() +
                " from account " + onUsDestinationAccount.getAccountNumber());

        return entityDTOMapper.toTransactionDTO(sourceTransaction);
    }
}
