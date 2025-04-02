package AbsaRBB.controller;

import AbsaRBB.OffUsTransactionService;
import AbsaRBB.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offUs")
public class OffUsTransactionController {

    private final OffUsTransactionService offUsTransactionService;

    @Autowired
    public OffUsTransactionController(OffUsTransactionService offUsTransactionService) {
        this.offUsTransactionService = offUsTransactionService;
    }

    @PostMapping("/external")
    public ResponseEntity<TransactionDTO> createExternalTransaction(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = offUsTransactionService.createExternalTransaction(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
}
