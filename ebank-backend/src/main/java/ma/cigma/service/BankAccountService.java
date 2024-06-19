package ma.cigma.service;

import ma.cigma.dto.*;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;

public interface BankAccountService {


    BankAccount createBankAccount(BankAccountRequestDTO requestDTO) throws CustomerNotFoundException;

    AccountOperationDTO transfer(TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficentException;
}
