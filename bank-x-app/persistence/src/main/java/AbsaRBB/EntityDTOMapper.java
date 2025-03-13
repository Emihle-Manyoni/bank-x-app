package AbsaRBB;

import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.CustomerEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.TransactionEntity;

import java.util.Date;

public class EntityDTOMapper {

    public static CustomerEntity toCustomerEntity(CustomerDTO customerDTO) {
        CustomerEntity customer = new CustomerEntity();
        customer.setCustomerID(customerDTO.getCustomerID());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setIdNumber(customerDTO.getIdNumber());
        customer.setEmailAddress(customerDTO.getEmailAddress());
        return customer;
    }

    public static CustomerDTO toCustomerDTO(CustomerEntity customerEntity) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomerID(customerEntity.getCustomerID());
        customerDTO.setFirstName(customerEntity.getFirstName());
        customerDTO.setLastName(customerEntity.getLastName());
        customerDTO.setIdNumber(customerEntity.getIdNumber());
        customerDTO.setEmailAddress(customerEntity.getEmailAddress());
        return customerDTO;
    }

    public static AccountsDTO toAccountDTO(AccountsEntity accountEntity) {
        AccountsDTO accountDTO = new AccountsDTO();
        accountDTO.setAccountID(accountEntity.getAccountID());
        accountDTO.setAccountType(accountEntity.getAccountType());
        accountDTO.setAccountNumber(accountEntity.getAccountNumber());
        accountDTO.setBalance(accountEntity.getBalance());
        accountDTO.setCreditedAmount(accountEntity.getCreditedAmount());
        accountDTO.setInterestRate(accountEntity.getInterestRate());

        // Only set the customer ID, not the whole customer object
        if (accountEntity.getCustomer() != null) {
            accountDTO.setCustomerID(accountEntity.getCustomer().getCustomerID());
        }

        return accountDTO;
    }

    public static AccountsEntity toAccountEntity(AccountsDTO accountDTO) {
        AccountsEntity accountEntity = new AccountsEntity();
        accountEntity.setAccountID(accountDTO.getAccountID());
        accountEntity.setAccountType(accountDTO.getAccountType());
        accountEntity.setAccountNumber(accountDTO.getAccountNumber());
        accountEntity.setBalance(accountDTO.getBalance());
        accountEntity.setCreditedAmount(accountDTO.getCreditedAmount());
        accountEntity.setInterestRate(accountDTO.getInterestRate());

        if (accountEntity.getInterestRate() == 0) {
            if ("Savings".equals(accountDTO.getAccountType())) {
                accountEntity.setInterestRate(5.0);
            } else if ("Current".equals(accountDTO.getAccountType())) {
                accountEntity.setInterestRate(0.5);
            }
        }
        return accountEntity;
    }
    public TransactionEntity toTransactionEntity(TransactionDTO dto, AccountsEntity account, ExternalBankEntity externalBank) {
        TransactionEntity entity = new TransactionEntity();

        entity.setTransactionID(dto.getTransactionID());
        entity.setAccount(account);
        entity.setExternalBank(externalBank);
        entity.setTransactionAmount((int) dto.getTransactionAmount());
        entity.setTransactionType(dto.getTransactionType());
        entity.setTransactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : new Date());
        entity.setCreatedDate(new Date());
        entity.setExternal(dto.isExternal());
        entity.setTransactionCharge(dto.getTransactionCharge());

        return entity;
    }

    public TransactionDTO toTransactionDTO(TransactionEntity entity) {
        TransactionDTO dto = new TransactionDTO();

        dto.setTransactionID(entity.getTransactionID());
        dto.setAccountID(entity.getAccount() != null ? entity.getAccount().getAccountID() : null);
        dto.setExternalBankID(entity.getExternalBank() != null ? entity.getExternalBank().getExternalBankID() : null);
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionType(entity.getTransactionType());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setExternal(entity.isExternal());
        dto.setTransactionCharge(entity.getTransactionCharge());

        return dto;
    }

}


