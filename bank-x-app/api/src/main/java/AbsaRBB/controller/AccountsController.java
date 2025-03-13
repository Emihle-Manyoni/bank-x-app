package AbsaRBB.controller;

import AbsaRBB.AccountService;
import AbsaRBB.dto.AccountsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {

    private final AccountService accountService;

    @Autowired
    public AccountsController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountsDTO> getAccountByNumber(@PathVariable("accountNumber") Integer accountNumber) {
        AccountsDTO account = accountService.getAccountByNumber(accountNumber);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountsDTO>> getAccountsByCustomerId(@PathVariable("customerId") Integer customerId) {
        List<AccountsDTO> accounts = accountService.getAccountsByCustomerId(customerId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }
}