package AbsaRBB;

import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    private final AccountsDomainRepo accountsDomainRepo;
    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;

    @Autowired
    public CustomerService(AccountsDomainRepo accountsDomainRepo,
                           CustomerRepository customerRepository,
                           AccountsRepository accountsRepository) {
        this.accountsDomainRepo = accountsDomainRepo;
        this.customerRepository = customerRepository;
        this.accountsRepository = accountsRepository;
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = EntityDTOMapper.toCustomerEntity(customerDTO);
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        CustomerDTO savedCustomerDTO = EntityDTOMapper.toCustomerDTO(savedCustomer);

        AccountsDTO currentAccount = new AccountsDTO();
        currentAccount.setCustomerID(savedCustomerDTO.getCustomerID());
        currentAccount.setAccountType("Current");
        currentAccount.setAccountNumber(generateAccountNumber());
        currentAccount.setCreditedAmount(0.0);
        currentAccount.setBalance(0.0);
        AccountsDTO savedCurrentAccount = accountsDomainRepo.save(currentAccount);

        AccountsDTO savingsAccount = new AccountsDTO();
        savingsAccount.setCustomerID(savedCustomerDTO.getCustomerID());
        savingsAccount.setAccountType("Savings");
        savingsAccount.setAccountNumber(generateAccountNumber());
        savingsAccount.setCreditedAmount(500.0);
        savingsAccount.setBalance(500.0);
        AccountsDTO savedSavingsAccount = accountsDomainRepo.save(savingsAccount);

        savedCustomerDTO.getAccounts().add(savedCurrentAccount);
        savedCustomerDTO.getAccounts().add(savedSavingsAccount);

        return savedCustomerDTO;
    }

    public CustomerDTO getCustomerById(Long customerID) {
        Optional<CustomerEntity> customerEntityOpt = customerRepository.findById(customerID);

        if (customerEntityOpt.isPresent()) {
            CustomerEntity customerEntity = customerEntityOpt.get();
            CustomerDTO customerDTO = EntityDTOMapper.toCustomerDTO(customerEntity);

            List<AccountsDTO> customerAccounts = accountsRepository.findByCustomerCustomerID(customerID)
                    .stream()
                    .map(EntityDTOMapper::toAccountDTO)
                    .toList();

            customerDTO.setAccounts(customerAccounts);
            return customerDTO;
        } else {
            throw new RuntimeException("Customer not found with ID: " + customerID);
        }
    }

    private int generateAccountNumber() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}