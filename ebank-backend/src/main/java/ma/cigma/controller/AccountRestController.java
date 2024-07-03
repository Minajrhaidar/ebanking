package ma.cigma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.BankAccountRequestDTO;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1/accounts")
@CrossOrigin("*")
public class AccountRestController {
    private BankAccountService bankAccountService;

    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @PostMapping("/create")
    public ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccountRequestDTO request) {
        try {
            BankAccount bankAccount = bankAccountService.createBankAccount(request);
            return new ResponseEntity<>(bankAccount, HttpStatus.CREATED);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @GetMapping
    public Page<BankAccount> getAllAccounts(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        return bankAccountService.getAllAccounts(page, size);
    }
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @GetMapping("/rib/{rib}")
    public ResponseEntity<BankAccount> getAccountByRib(@PathVariable String rib) {
        try {
            BankAccount account = bankAccountService.getAccountByRib(rib);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (BankAccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @GetMapping("/search")
    public Page<BankAccount> searchAccounts(@RequestParam String keyword,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "8") int size) {
        return bankAccountService.searchAccounts(keyword, page, size);
    }

    @PreAuthorize("hasAnyRole('CROLE_AGENT_GUICHET', 'ROLE_CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable Long id) {
        try {
            BankAccount account = bankAccountService.getAccountById(id);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (BankAccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            bankAccountService.deleteAccount(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (BankAccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable Long id, @RequestBody BankAccountRequestDTO request) {
        try {
            BankAccount updatedAccount = bankAccountService.updateAccount(id, request);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (BankAccountNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
