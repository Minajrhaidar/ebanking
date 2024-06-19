package ma.cigma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.*;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/accounts")
@CrossOrigin("*")
@PreAuthorize("hasRole('AGENT_GUICHET')")
public class AccountRestController {
    private BankAccountService bankAccountService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREATE_BANK_ACCOUNT')")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccountRequestDTO request) {
        try {
            BankAccount bankAccount = bankAccountService.createBankAccount(request);
            return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
