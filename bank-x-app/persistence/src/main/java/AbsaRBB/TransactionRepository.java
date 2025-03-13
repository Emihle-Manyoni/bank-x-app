package AbsaRBB;
import AbsaRBB.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{
    List<TransactionEntity> findByAccountAccountID(Long accountID);
    List<TransactionEntity>findByIsExternal(boolean isExternal);
}
