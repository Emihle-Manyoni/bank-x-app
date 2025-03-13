package AbsaRBB;

import AbsaRBB.entity.AccountsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<AccountsEntity, Long> {
    Optional<AccountsEntity> findByAccountNumber(int accountNumber);
    List<AccountsEntity> findByCustomerCustomerID(Integer customerID);
}
