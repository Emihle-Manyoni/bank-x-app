package AbsaRBB;
import AbsaRBB.dto.AccountsDTO;
import AbsaRBB.entity.AccountsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountsDomainRepo {
    private final AccountsRepository accountsRepository;
    private final CustomerRepository customerRepository;

    @Autowired
    public AccountsDomainRepo(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }

    public AccountsDTO findByAccountNumber(int accountNumber) {
        Optional<AccountsEntity> account = this.accountsRepository.findByAccountNumber(accountNumber);

        if (account.isPresent()) {
            return EntityDTOMapper.toAccountDTO(account.get());
        } else {
            throw new RuntimeException("Account not found with account number: " + accountNumber);
        }
    }

    public AccountsDTO save(AccountsDTO accountsDTO) {
        AccountsEntity accountsEntity = EntityDTOMapper.toAccountEntity(accountsDTO);

        if (accountsDTO.getCustomerID() != null) {
           customerRepository.findById(accountsDTO.getCustomerID())
                    .ifPresent(accountsEntity::setCustomer);
        }

        AccountsEntity savedEntity = this.accountsRepository.save(accountsEntity);

        return EntityDTOMapper.toAccountDTO(savedEntity);
    }
}
