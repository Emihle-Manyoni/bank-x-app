package AbsaRBB.controller;

import AbsaRBB.OnUsTransactionService;
import AbsaRBB.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/onUs")
public class OnUsTransactionController {

    private final OnUsTransactionService onUsTransactionService;

    @Autowired
    public OnUsTransactionController(OnUsTransactionService onUsTransactionService) {
        this.onUsTransactionService = onUsTransactionService;
    }

    @PostMapping("/payment")
    public ResponseEntity<TransactionDTO> createPayment(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = onUsTransactionService.createPayment(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

}
