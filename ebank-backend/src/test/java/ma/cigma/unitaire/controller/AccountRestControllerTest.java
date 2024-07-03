package ma.cigma.unitaire.controller;

import ma.cigma.controller.AccountRestController;
import ma.cigma.dto.BankAccountRequestDTO;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountRestControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private AccountRestController accountRestController;

    public AccountRestControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    // createBankAccount tests
    @Test
    public void testCreateBankAccount_Success() throws CustomerNotFoundException {
        BankAccountRequestDTO request = new BankAccountRequestDTO();
        BankAccount bankAccount = new BankAccount();

        when(bankAccountService.createBankAccount(request)).thenReturn(bankAccount);

        ResponseEntity<BankAccount> response = accountRestController.createBankAccount(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(bankAccount, response.getBody());

        verify(bankAccountService, times(1)).createBankAccount(request);
    }

    @Test
    public void testCreateBankAccount_CustomerNotFoundException() throws CustomerNotFoundException {
        BankAccountRequestDTO request = new BankAccountRequestDTO();

        when(bankAccountService.createBankAccount(request)).thenThrow(new CustomerNotFoundException());

        ResponseEntity<BankAccount> response = accountRestController.createBankAccount(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(bankAccountService, times(1)).createBankAccount(request);
    }

    // getAccountByRib tests
    @Test
    public void testGetAccountByRib_Success() throws BankAccountNotFoundException {
        String rib = "123456789";
        BankAccount bankAccount = new BankAccount();

        when(bankAccountService.getAccountByRib(rib)).thenReturn(bankAccount);

        ResponseEntity<BankAccount> response = accountRestController.getAccountByRib(rib);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankAccount, response.getBody());

        verify(bankAccountService, times(1)).getAccountByRib(rib);
    }

    @Test
    public void testGetAccountByRib_BankAccountNotFoundException() throws BankAccountNotFoundException {
        String rib = "123456789";

        when(bankAccountService.getAccountByRib(rib)).thenThrow(new BankAccountNotFoundException());

        ResponseEntity<BankAccount> response = accountRestController.getAccountByRib(rib);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(bankAccountService, times(1)).getAccountByRib(rib);
    }
}
