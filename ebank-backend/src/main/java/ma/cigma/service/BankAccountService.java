package ma.cigma.service;

import ma.cigma.dto.*;
import ma.cigma.entity.BankAccount;
import ma.cigma.exception.BakaccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {


    BankAccount createBankAccount(BankAccountRequestDTO requestDTO) throws CustomerNotFoundException;

    AccountOperationDTO transfer(TransferRequestDTO transferRequestDTO) throws BakaccountNotFoundException, BalanceNotSufficentException;
}
