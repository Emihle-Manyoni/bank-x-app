package AbsaRBB;
import AbsaRBB.entity.ReconciliationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReconciliationRepository extends JpaRepository<ReconciliationEntity, Long>{
}
