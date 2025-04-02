package AbsaRBB;

import AbsaRBB.dto.TransactionDTO;
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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Mock
    private AccountsRepository accountsRepository;


    @Mock
    private EntityDTOMapper entityDTOMapper;

    @InjectMocks
    private TransactionService transactionService;


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
