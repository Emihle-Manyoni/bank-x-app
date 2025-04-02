package AbsaRBB;

import AbsaRBB.Exceptions.AccountNotFoundException;
import AbsaRBB.Exceptions.CustomerNotFoundException;
import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.dto.CustomerDTO;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.AccountsEntity;
import AbsaRBB.entity.CustomerEntity;
import AbsaRBB.entity.ExternalBankEntity;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    private final AccountsDomainRepo accountsDomainRepo;
    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;
    private final EntityDTOMapper entityDTOMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    @Autowired
    public CustomerService(AccountsDomainRepo accountsDomainRepo,
                           CustomerRepository customerRepository,
                           AccountsRepository accountsRepository, EntityDTOMapper entityDTOMapper, TransactionRepository transactionRepository, TransactionService transactionService) {
        this.accountsDomainRepo = accountsDomainRepo;
        this.customerRepository = customerRepository;
        this.accountsRepository = accountsRepository;
        this.entityDTOMapper = entityDTOMapper;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
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

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAccountID(savedSavingsAccount.getAccountID());
        transactionDTO.setTransactionAmount(500.00);
        transactionDTO.setTransactionType("Savings");
        transactionDTO.setTransactionDate(new Date());
        transactionDTO.setCreatedDate(new Date());
        transactionDTO.setExternal(false);

        TransactionDTO savedTransaction = transactionService.createSavingsAccountTransaction(transactionDTO);

        return savedCustomerDTO;
    }



    public CustomerDTO getCustomerById(Long customerID) {
        CustomerEntity customerEntityOpt = customerRepository.findById(customerID)
                .orElseThrow(() -> new CustomerNotFoundException(customerID));

            CustomerDTO customerDTO = EntityDTOMapper.toCustomerDTO(customerEntityOpt);

            List<AccountsDTO> customerAccounts = accountsRepository.findByCustomerCustomerID(customerID)
                    .stream()
                    .map(EntityDTOMapper::toAccountDTO)
                    .toList();

            customerDTO.setAccounts(customerAccounts);
            return customerDTO;

    }

    private int generateAccountNumber() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}