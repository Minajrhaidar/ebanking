package ma.cigma.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.AccountOperationDTO;
import ma.cigma.dto.TransferRequestDTO;
import ma.cigma.exception.BakaccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class BankAccountController {

    private BankAccountService bankAccountService;

    @PostMapping("/transfer")
    public ResponseEntity<AccountOperationDTO> transfer(@RequestBody TransferRequestDTO transferRequest) throws BakaccountNotFoundException, BalanceNotSufficentException {
        AccountOperationDTO operationDTO = bankAccountService.transfer(transferRequest);
        return new ResponseEntity<>(operationDTO, HttpStatus.CREATED);
    }

}