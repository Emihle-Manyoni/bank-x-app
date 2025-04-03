package AbsaRBB;

import AbsaRBB.Exceptions.BadRequestException;
import AbsaRBB.Exceptions.DestinationAccountNotFoundException;
import AbsaRBB.Exceptions.InsufficientFundsException;
import AbsaRBB.Exceptions.SourceAccountNotFoundException;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class InternalTransfersService {


    private final AccountsRepository accountsRepository;
    private final TransactionRepository transactionRepository;
    private final EntityDTOMapper entityDTOMapper;

    @Autowired
    public InternalTransfersService(AccountsRepository accountsRepository, TransactionRepository transactionRepository, EntityDTOMapper entityDTOMapper) {
        this.accountsRepository = accountsRepository;
        this.transactionRepository = transactionRepository;
        this.entityDTOMapper = entityDTOMapper;
    }

    @Transactional
    public TransactionDTO createInternalTransfer(TransactionDTO transactionDTO) {
        AccountsEntity sourceAccount = accountsRepository.findById(transactionDTO.getSourceAccountID())
                .orElseThrow(() -> new SourceAccountNotFoundException(transactionDTO.getSourceAccountID()));

        AccountsEntity destinationAccount = accountsRepository.findById(transactionDTO.getDestinationAccountID())
                .orElseThrow(() -> new DestinationAccountNotFoundException(transactionDTO.getDestinationAccountID()));

        if (!sourceAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID()) ||
                !destinationAccount.getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
            throw new BadRequestException("Customer verification failed. customer does not own one or both of the accounts .");
        }



        double totalDeduction = transactionDTO.getTransactionAmount();
        if (sourceAccount.getBalance() < totalDeduction) {
            throw new InsufficientFundsException();
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
        transactionRepository.save(sourceTransaction);

        TransactionEntity destTransaction = new TransactionEntity();
        destTransaction.setAccount(destinationAccount);
        destTransaction.setTransactionAmount((int) transactionDTO.getTransactionAmount());
        destTransaction.setTransactionType("Transfer In");
        destTransaction.setTransactionDate(new Date());
        destTransaction.setCreatedDate(new Date());
        destTransaction.setExternal(false);
        transactionRepository.save(destTransaction);

        return entityDTOMapper.toTransactionDTO(sourceTransaction);
    }
}
