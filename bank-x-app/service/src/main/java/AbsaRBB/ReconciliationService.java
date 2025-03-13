package AbsaRBB;

import AbsaRBB.ReconciliationRepository;
import AbsaRBB.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReconciliationService {
    private final ReconciliationRepository reconciliationRepository;

    @Autowired
    public ReconciliationService(ReconciliationRepository reconciliationRepository) {
        this.reconciliationRepository = reconciliationRepository;
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void createBatchJob(){

    }


}
