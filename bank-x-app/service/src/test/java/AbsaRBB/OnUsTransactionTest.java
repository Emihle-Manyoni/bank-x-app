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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnUsTransactionTest {
    @Mock
    private TransactionRepository transactionRepository;
    private static final double TRANSACTION_FEE_RATE = 0.0005;

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private EntityDTOMapper entityDTOMapper;

    @InjectMocks
    private OnUsTransactionService onUsTransactionService;

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

        TransactionDTO createdPayment = onUsTransactionService.createPayment(transactionDTO);

        //THEN
        Assertions.assertNotNull(createdPayment);
        Assertions.assertEquals(1L, createdPayment.getTransactionID());
        Assertions.assertEquals(1L, createdPayment.getAccountID());
        Assertions.assertEquals("Payment", createdPayment.getTransactionType());
    }
}
