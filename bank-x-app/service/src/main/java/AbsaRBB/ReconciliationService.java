package AbsaRBB;

import AbsaRBB.ReconciliationRepository;
import AbsaRBB.dto.TransactionDTO;
import AbsaRBB.entity.ReconciliationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReconciliationService {
    private final ReconciliationRepository reconciliationRepository;
    private final TransactionRepository transactionRepository;
    private final EntityDTOMapper entityDTOMapper;

    @Autowired
    public ReconciliationService(ReconciliationRepository reconciliationRepository, TransactionRepository transactionRepository, EntityDTOMapper entityDTOMapper) {
        this.reconciliationRepository = reconciliationRepository;
        this.transactionRepository = transactionRepository;
        this.entityDTOMapper = entityDTOMapper;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void processExternalTransaction(){
        System.out.println("Starting reconciliation process at " + new Date());

        List<TransactionDTO> transactionDTOs = transactionRepository.findByIsExternal(true)
                .stream()
                .map(entityDTOMapper::toTransactionDTO)
                .collect(Collectors.toList());

        List<ReconciliationEntity> reconciliations = reconciliationRepository.findAll();

        int matchedCount = 0;

        for (ReconciliationEntity recon : reconciliations) {
            if (!"Unmatched".equals(recon.getReconciliationStatus())) {
                continue;
            }
            for (TransactionDTO transaction : transactionDTOs) {
                if (transaction.getTransactionAmount() == recon.getTransactionAmount() &&
                        transaction.getTransactionType().equals(recon.getTransactionType()) &&
                        transaction.getTransactionCharge() == recon.getTransactionCharge()) {

                    recon.setReconciliationStatus("Matched");
                    recon.setStatus("Processed");
                    reconciliationRepository.save(recon);

                    matchedCount++;
                    break;
                }
            }
        }
        System.out.println("Reconciliation completed: " + matchedCount + " records processed");
    }
}
