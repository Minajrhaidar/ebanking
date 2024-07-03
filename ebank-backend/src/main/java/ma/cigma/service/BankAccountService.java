package ma.cigma.service;

import ma.cigma.dto.*;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BankAccountService {


    BankAccount createBankAccount(BankAccountRequestDTO requestDTO) throws CustomerNotFoundException;

//    List<BankAccount> getAllAccounts();

    Page<BankAccount> getAllAccounts(int page, int size);

    BankAccount getAccountById(Long id) throws BankAccountNotFoundException;

    AccountOperationDTO transfer(TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficentException;

    void deleteAccount(Long id) throws BankAccountNotFoundException;

    BankAccount updateAccount(Long id, BankAccountRequestDTO request) throws BankAccountNotFoundException;

    BankAccount getAccountByRib(String rib) throws BankAccountNotFoundException;

//    List<BankAccount> searchAccounts(String keyword);

    Page<BankAccount> searchAccounts(String keyword, int page, int size);
}
