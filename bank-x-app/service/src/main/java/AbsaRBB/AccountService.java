package AbsaRBB;

import AbsaRBB.dto.AccountsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountsDomainRepo accountsDomainRepo;
    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountService(AccountsDomainRepo accountsDomainRepo, AccountsRepository accountsRepository) {
        this.accountsDomainRepo = accountsDomainRepo;
        this.accountsRepository = accountsRepository;
    }

    public AccountsDTO getAccountByNumber(int accountNumber) {
        return accountsDomainRepo.findByAccountNumber(accountNumber);
    }


    public List<AccountsDTO> getAccountsByCustomerId(Integer customerId) {
        return accountsRepository.findByCustomerCustomerID(customerId)
                .stream()
                .map(EntityDTOMapper::toAccountDTO)
                .collect(Collectors.toList());
    }
}