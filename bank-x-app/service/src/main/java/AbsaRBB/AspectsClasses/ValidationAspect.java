package AbsaRBB.AspectsClasses;

import AbsaRBB.AccountsRepository;
import AbsaRBB.Exceptions.AccountNotFoundException;
import AbsaRBB.Exceptions.BadRequestException;
import AbsaRBB.Exceptions.ExternalBankAccountNotFoundException;
import AbsaRBB.Exceptions.InsufficientFundsException;
import AbsaRBB.ExternalBankRepository;
import AbsaRBB.TransactionService;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.ExternalBankEntity;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Aspect
@Component
@NoArgsConstructor
public class ValidationAspect {

    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private ExternalBankRepository externalBankRepository;

    private void validateCustomerAccount(TransactionDTO transactionDTO){
        Optional<AccountsEntity> account = accountsRepository.findById(transactionDTO.getAccountID());
        if (!account.get().getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
            throw new BadRequestException("Customer verification failed. Account does not belong to the specified customer.");
        }
    }

    private void validateTransactionCreationDate(TransactionDTO transactionDTO){
        transactionDTO.setCreatedDate(new Date());
        if (transactionDTO.getTransactionDate() == null) {
            transactionDTO.setTransactionDate(new Date());
        }

    }

    private void setValidationType(TransactionDTO transactionDTO){
        if (transactionDTO.getTransactionType() == null || transactionDTO.getTransactionType().isEmpty()) {
            transactionDTO.setTransactionType("Payment");
        }
    }

    private void validateIfExternalPayment(TransactionDTO transactionDTO){
        ExternalBankEntity externalBank = null;
        if (transactionDTO.getExternalBankID() != null) {
            externalBank = externalBankRepository.findById(transactionDTO.getExternalBankID())
                    .orElse(null);
            transactionDTO.setExternal(true);
        } else {
            transactionDTO.setExternal(false);
        }
    }

    @Around("execution(* AbsaRBB.TransactionService.createPayment(..))")
    public Object validateCreationOfPayment(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
     Object [] args = proceedingJoinPoint.getArgs();
     if(args.length > 0 && args[0] instanceof TransactionDTO){
         TransactionDTO transactionDTO = (TransactionDTO) args[0];

         validateCustomerAccount(transactionDTO);
         validateTransactionCreationDate(transactionDTO);
         setValidationType(transactionDTO);
         validateIfExternalPayment(transactionDTO);

         return proceedingJoinPoint.proceed(new Object[]{transactionDTO});

         }
        System.out.println("Payment Created Successfully");
     return proceedingJoinPoint.proceed();
    }

    private static final double TRANSACTION_FEE_RATE = 0.0005;
    private void validateDebitOrCredit(TransactionDTO transactionDTO){
        Optional<AccountsEntity> account = accountsRepository.findById(transactionDTO.getAccountID());

        if (!account.isPresent()) {
            throw new AccountNotFoundException(transactionDTO.getAccountID());
        }
        AccountsEntity accountsEntity = account.get();

        double transactionFee = transactionDTO.getTransactionAmount() * TRANSACTION_FEE_RATE;
        transactionDTO.setTransactionCharge(transactionFee);

        if ("Credit".equalsIgnoreCase(transactionDTO.getTransactionType())) {
            double newBalance = account.get().getBalance() + transactionDTO.getTransactionAmount() - transactionFee;
        } else if ("Debit".equalsIgnoreCase(transactionDTO.getTransactionType())) {
            double newBalance = account.get().getBalance() - transactionDTO.getTransactionAmount() - transactionFee;
            if (newBalance < 0) {
                throw new InsufficientFundsException();
            }
        } else {
            throw new BadRequestException("Invalid transaction type. Must be 'Credit' or 'Debit'.");
        }

    }
    public void setDateForExternalTransaction(TransactionDTO transactionDTO){
        transactionDTO.setCreatedDate(new Date());
        if (transactionDTO.getTransactionDate() == null) {
            transactionDTO.setTransactionDate(new Date());
        }
    }
    public void setToExternalBank(TransactionDTO transactionDTO){
        transactionDTO.setExternal(true);
    }

    @Around("execution(* AbsaRBB.TransactionService.createExternalTransaction(..))")
    public Object validateCreationOfExternalTransaction(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object [] args = proceedingJoinPoint.getArgs();

        if(args.length > 0 && args[0] instanceof TransactionDTO){
            TransactionDTO transactionDTO = (TransactionDTO) args[0];

            Optional<ExternalBankEntity> externalBankOptional =
                    externalBankRepository.findById(transactionDTO.getExternalBankID());

            if (!externalBankOptional.isPresent()) {
                throw new ExternalBankAccountNotFoundException(transactionDTO.getExternalBankID());
            }
            setToExternalBank(transactionDTO);
            validateDebitOrCredit(transactionDTO);
            setDateForExternalTransaction(transactionDTO);

            return proceedingJoinPoint.proceed(new Object[]{transactionDTO});
        }
        return proceedingJoinPoint.proceed();
    }
}
