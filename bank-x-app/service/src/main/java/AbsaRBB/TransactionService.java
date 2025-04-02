package AbsaRBB;

import AbsaRBB.Exceptions.AccountNotFoundException;
import AbsaRBB.Exceptions.TransactionNotFoundException;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final AccountsRepository accountsRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, EntityDTOMapper entityDTOMapper, AccountsRepository accountsRepository) {
        this.transactionRepository = transactionRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.accountsRepository = accountsRepository;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(entityDTOMapper::toTransactionDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        return entityDTOMapper.toTransactionDTO(transaction);
    }

    public TransactionDTO createSavingsAccountTransaction(TransactionDTO transactionDTO){

        AccountsEntity saveSavingsAccount = accountsRepository.findById(transactionDTO.getAccountID())
                .orElseThrow(() -> new AccountNotFoundException(transactionDTO.getAccountID()));

        ExternalBankEntity externalBank = null;
        accountsRepository.save(saveSavingsAccount);

        TransactionEntity transactionEntity = entityDTOMapper.toTransactionEntity(transactionDTO, saveSavingsAccount ,externalBank);
        TransactionEntity savedTransaction = transactionRepository.save(transactionEntity);

        return transactionDTO;
    }

}
