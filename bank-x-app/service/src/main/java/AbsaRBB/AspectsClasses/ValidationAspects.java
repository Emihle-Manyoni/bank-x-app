//package AbsaRBB.AspectsClasses;
//
//import AbsaRBB.AccountsRepository;
//import AbsaRBB.entity.AccountsEntity;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import AbsaRBB.dto.*;
//
//import java.util.Optional;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//public class ValidationAspects {
//
//    private final AccountsRepository accountsRepository;
//    private static final double TRANSACTION_FEE_RATE = 0.0005;
//
//    @Before("execution(* AbsaRBB.TransactionService.createPayment(..))")
//    public void validateCustomer(){
//        Optional<AccountsEntity> account = accountsRepository.findById(transactionDTO.getAccountID());
//        if(account.isPresent())
//        {
//            if (!account.get().getCustomer().getCustomerID().equals(transactionDTO.getCustomerID())) {
//                throw new RuntimeException("Customer verification failed. Account does not belong to the specified customer.");
//            }
//        } else
//        double newBalance = account.get().getBalance() - transactionDTO.getTransactionAmount() - transactionFee;
//
//        if (newBalance < 0) {
//            throw new RuntimeException("Insufficient funds for this transaction");
//        }
//        }
//    }
//
//
//
//
//
