package AbsaRBB;
import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    private final AccountsDomainRepo accountsDomainRepo;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(AccountsDomainRepo accountsDomainRepo, CustomerRepository customerRepository) {
        this.accountsDomainRepo = accountsDomainRepo;
        this.customerRepository = customerRepository;
    }


    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        // First save the customer to get an ID
        CustomerEntity customerEntity = EntityDTOMapper.toCustomerEntity(customerDTO);
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        CustomerDTO savedCustomerDTO = EntityDTOMapper.toCustomerDTO(savedCustomer);

        // Create current account with zero balance
        AccountsDTO currentAccount = new AccountsDTO();
        currentAccount.setCustomerID(savedCustomerDTO.getCustomerID());
        currentAccount.setAccountType("Current");
        currentAccount.setAccountNumber(generateAccountNumber());
        currentAccount.setCreditedAmount(0.0);
        currentAccount.setBalance(0.0);
        accountsDomainRepo.save(currentAccount);

        // Create savings account with R500 initial bonus
        AccountsDTO savingsAccount = new AccountsDTO();
        savingsAccount.setCustomerID(savedCustomerDTO.getCustomerID());
        savingsAccount.setAccountType("Savings");
        savingsAccount.setAccountNumber(generateAccountNumber());
        savingsAccount.setCreditedAmount(500.0); // Initial bonus of R500
        savingsAccount.setBalance(500.0);        // Initial balance = bonus amount
        accountsDomainRepo.save(savingsAccount);

        return savedCustomerDTO;
    }


    public CustomerDTO getCustomerById(Long customerID) {
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerID);

        if (customerEntity.isPresent()) {
            return EntityDTOMapper.toCustomerDTO(customerEntity.get());
        } else {
            throw new RuntimeException("Customer not found with ID: " + customerID);
        }
    }


    private int generateAccountNumber() {
        // Generate a random 6-digit account number (100000-999999)
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
