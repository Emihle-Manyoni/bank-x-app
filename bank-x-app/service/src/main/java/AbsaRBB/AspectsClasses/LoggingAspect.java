package AbsaRBB.AspectsClasses;

import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.dto.TransactionDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;


@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* AbsaRBB.CustomerService.*(..))")
    public void loggingMethods(){}

//    @AfterThrowing(pointcut = "loggingMethods", throwing = "exception")
//    public void loggingExceptions(JoinPoint joinPoint, GlobalExceptionsHandler globalExceptionsHandler) {
//        log.error("Exception in {}: {} - {}",
//                joinPoint.getSignature().getName(),
//                globalExceptionsHandler.getClass().getName());
//    }

    @AfterReturning(pointcut = "execution(* AbsaRBB.CustomerService.createCustomer(..))", returning = "customer")
    public void logCustomerCreation(CustomerDTO customer) {
        log.info("Successfully created customer with ID: {}, with savings account: {} and current account: {}",
                customer.getCustomerID(),
                customer.getAccounts());
    }

    @AfterReturning(pointcut = "execution(* AbsaRBB.CustomerService.createCustomer(..))", returning = "transaction")
    public void logCreatedPayment(TransactionDTO transactionDTO){
        log.info("Payment Created Successfully from account: {}, from customer ID: {}, payment Amount: {} ",
                transactionDTO.getAccountID(),
                transactionDTO.getCustomerID(),
                transactionDTO.getTransactionAmount());
    }

    @AfterReturning(pointcut = "execution(* AbsaRBB.CustomerService.createExternalTransaction(..))", returning = "transactionBoniswa1@$$")
    public void logCreatedExternalTransaction(TransactionDTO transactionDTO){
        log.info("Payment Created Successfully from account: {}, ByExternal account: {}, payment Amount: {} ",
                transactionDTO.getAccountID(),
                transactionDTO.getExternalBankID(),
                transactionDTO.getTransactionAmount());
    }

}
