package ma.cigma.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.cigma.dto.AccountOperationDTO;
import ma.cigma.dto.TransferRequestDTO;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agent/accounts")
@AllArgsConstructor
public class BankAccountController {

    private BankAccountService bankAccountService;

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('ROLE_AGENT_GUICHET', 'ROLE_CLIENT')")
    public ResponseEntity<AccountOperationDTO> transfer(@Valid @RequestBody TransferRequestDTO transferRequest) throws BankAccountNotFoundException, BalanceNotSufficentException {
        AccountOperationDTO operationDTO = bankAccountService.transfer(transferRequest);
        return new ResponseEntity<>(operationDTO, HttpStatus.CREATED);
    }

}
