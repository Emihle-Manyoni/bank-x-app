package AbsaRBB.controller;

import AbsaRBB.AccountService;
import AbsaRBB.InternalTransfersService;
import AbsaRBB.dto.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/transfers")
public class InternalTransfersController {

    private final InternalTransfersService internalTransfersService;
    @Autowired
    public InternalTransfersController(InternalTransfersService internalTransfersService) {
        this.internalTransfersService = internalTransfersService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> createInternalTransfer(@RequestBody TransactionDTO transactionDTO) {
        TransactionDTO createdTransaction = internalTransfersService.createInternalTransfer(transactionDTO);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }
}
