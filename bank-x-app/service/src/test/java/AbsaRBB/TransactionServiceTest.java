package AbsaRBB;

import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.TransactionEntity;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private ExternalBankRepository externalBankRepository;

    @Mock
    private AccountsDomainRepo accountsDomainRepo;

    @Mock
    private ReconciliationRepository reconciliationRepository;

    @Mock
    private EntityDTOMapper entityDTOMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void Create_Payment_Test_From_Internal_Account_To_External_Account_TransactionService() {
        //Given
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountID(1L);
        transactionDTO.setTransactionAmount(50.00);
        transactionDTO.setTransactionType("Payment");

        AccountsEntity accountsEntity = new AccountsEntity();
        accountsEntity.setAccountID(1L);
        accountsEntity.setAccountNumber(123);
        accountsEntity.setBalance(200.00);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionID(1L);
        transactionEntity.setAccount(accountsEntity);
        transactionEntity.setTransactionAmount(50.00);
        transactionEntity.setTransactionType("Payment");

        TransactionDTO expectedDTO = new TransactionDTO();
        expectedDTO.setTransactionID(1L);
        expectedDTO.setAccountID(1L);
        expectedDTO.setTransactionAmount(50.00);
        expectedDTO.setTransactionType("Payment");

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        //WHEN
        when(accountsRepository.findById(1L)).thenReturn(Optional.of(accountsEntity));
        when(entityDTOMapper.toTransactionEntity(eq(transactionDTO), eq(accountsEntity), isNull())).thenReturn(transactionEntity);
        when(transactionRepository.save(transactionEntity)).thenReturn(transactionEntity);
        when(entityDTOMapper.toTransactionDTO(transactionEntity)).thenReturn(expectedDTO);

        TransactionDTO createdPayment = transactionService.createPayment(transactionDTO);

        //THEN
        Assertions.assertNotNull(createdPayment);
        Assertions.assertEquals(1L, createdPayment.getTransactionID());
        Assertions.assertEquals(1L, createdPayment.getAccountID());
        Assertions.assertEquals("Payment", createdPayment.getTransactionType());
    }

    @Test
    public void finding_Transactions_By_ID_Test_TransactionService(){
        //Given

        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionID(1L);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionID(1L);

        //WHEN
        when(entityDTOMapper.toTransactionDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO foundTransaction = entityDTOMapper.toTransactionDTO(transaction);

        //THEN
        Assertions.assertNotNull(foundTransaction.getTransactionID());
    }

    @Test
    public void finding_All_Existing_Transactions_TransactionService(){
        // Given
        List<TransactionEntity> transactionList = new ArrayList<>();

        TransactionEntity firstTransaction = new TransactionEntity();
        firstTransaction.setTransactionID(1L);
        firstTransaction.setTransactionType("Payment");
        firstTransaction.setTransactionAmount(100.00);
        transactionList.add(firstTransaction);

        TransactionEntity secondTransaction = new TransactionEntity();
        secondTransaction.setTransactionID(2L);
        secondTransaction.setTransactionType("Deposit");
        secondTransaction.setTransactionAmount(200.00);
        transactionList.add(secondTransaction);

        TransactionDTO dtoFirstTransaction = new TransactionDTO();
        dtoFirstTransaction.setTransactionID(1L);
        dtoFirstTransaction.setTransactionType("Payment");
        dtoFirstTransaction.setTransactionAmount(100.00);

        TransactionDTO dtoSecondTransaction = new TransactionDTO();
        dtoSecondTransaction.setTransactionID(2L);
        dtoSecondTransaction.setTransactionType("Deposit");
        dtoSecondTransaction.setTransactionAmount(200.00);

        List<TransactionDTO> expectedDTOs = Arrays.asList(dtoFirstTransaction, dtoSecondTransaction);

        // When
        when(transactionRepository.findAll()).thenReturn(transactionList);
        when(entityDTOMapper.toTransactionDTO(firstTransaction)).thenReturn(dtoFirstTransaction);
        when(entityDTOMapper.toTransactionDTO(secondTransaction)).thenReturn(dtoSecondTransaction);

        List<TransactionDTO> foundAllTransactions = transactionService.getAllTransactions();

        // Then
        Assertions.assertNotNull(foundAllTransactions);
        Assertions.assertEquals(1L, foundAllTransactions.get(0).getTransactionID());
        Assertions.assertEquals(2L, foundAllTransactions.get(1).getTransactionID());
        Assertions.assertEquals("Payment", foundAllTransactions.get(0).getTransactionType());
        Assertions.assertEquals("Deposit", foundAllTransactions.get(1).getTransactionType());
    }
}
