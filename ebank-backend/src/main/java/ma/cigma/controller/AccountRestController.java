package ma.cigma.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.*;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BakaccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/accounts")
@CrossOrigin("*")
public class AccountRestController {
    private BankAccountService bankAccountService;

    @PostMapping("/create")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccountRequestDTO request) {
        try {
            BankAccount bankAccount = bankAccountService.createBankAccount(request);
            return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
