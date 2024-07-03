package ma.cigma.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.*;
import ma.cigma.entity.*;
import ma.cigma.entity.enums.AccountStatus;
import ma.cigma.entity.enums.OperationType;
import ma.cigma.exception.BankAccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.repository.AccountOperationRepository;
import ma.cigma.repository.BankAccountRepository;
import ma.cigma.repository.CustomerRepository;
import ma.cigma.service.BankAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

class BankAccountServiceImpl implements BankAccountService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final ModelMapper modelMapper;



    @Override
    public BankAccount createBankAccount(BankAccountRequestDTO requestDTO) throws CustomerNotFoundException {
        // RG_8 : Vérification de l'existence du client
        Optional<Customer> customerOptional = customerRepository.findById(requestDTO.getCustomerId());
        if (customerOptional.isEmpty()) {
            throw new CustomerNotFoundException("Customer with ID " + requestDTO.getCustomerId() + " not found.");
        }

        // RG_9 : Vérification du format du RIB (12 caractères)
        if (!isValidRibFormat(requestDTO.getRib())) {
            throw new IllegalArgumentException("Invalid RIB format. The RIB must have 12 characters.");
        }

        // RG_10 : Création du compte bancaire avec le statut "Ouvert"
        BankAccount bankAccount = new BankAccount();
        bankAccount.setRib(requestDTO.getRib());
        bankAccount.setStatus(AccountStatus.OPEN);
        bankAccount.setCustomer(customerOptional.get());
        bankAccount.setBalance(requestDTO.getBalance()); // Utilisez le solde initial fourni dans le DTO

        // Enregistrement du compte bancaire
        return bankAccountRepository.save(bankAccount);
    }

    // Méthode de validation du format du RIB (12 caractères)
    private boolean isValidRibFormat(String rib) {
        return rib.length() == 12;
    }

    @Override
    public Page<BankAccount> getAllAccounts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bankAccountRepository.findAll(pageable);
    }

    @Override
    public BankAccount getAccountById(Long id) throws BankAccountNotFoundException {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with ID: " + id));
    }

    @Override
    public AccountOperationDTO transfer(TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount senderAccount = bankAccountRepository.findByRib(transferRequestDTO.getSourceRib())
                .orElseThrow(() -> new BankAccountNotFoundException("Sender bank account not found"));

        if (senderAccount.getStatus() != AccountStatus.OPEN) {
            throw new IllegalStateException("Sender bank account is not open");
        }

        if (senderAccount.getBalance() < transferRequestDTO.getAmount()) {
            throw new BalanceNotSufficentException("Insufficient balance in sender bank account");
        }

        double amount = transferRequestDTO.getAmount();
        String description = transferRequestDTO.getDescription();

        // Débiter le montant du compte expéditeur
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        bankAccountRepository.save(senderAccount);

        // Enregistrer l'opération de retrait
        AccountOperation withdrawal = new AccountOperation();
        withdrawal.setOperationDate(new Date());
        withdrawal.setType(OperationType.WITHDRAWAL);
        withdrawal.setAmount(amount);
        withdrawal.setBankAccount(senderAccount);
        withdrawal.setDescription(description);
        accountOperationRepository.save(withdrawal);

        BankAccount receiverAccount = bankAccountRepository.findByRib(transferRequestDTO.getDestinationRib())
                .orElseThrow(() -> new BankAccountNotFoundException("Receiver bank account not found"));

        // Créditer le montant sur le compte destinataire
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);
        bankAccountRepository.save(receiverAccount);

        // Enregistrer l'opération de dépôt
        AccountOperation deposit = new AccountOperation();
        deposit.setOperationDate(new Date());
        deposit.setType(OperationType.DEPOSIT);
        deposit.setAmount(amount);
        deposit.setBankAccount(receiverAccount);
        deposit.setDescription(description);
        accountOperationRepository.save(deposit);

        // Retourner les détails de l'opération de transfert
        return modelMapper.map(withdrawal, AccountOperationDTO.class);
    }

    @Override
    public void deleteAccount(Long id) throws BankAccountNotFoundException {
        if (!bankAccountRepository.existsById(id)) {
            throw new BankAccountNotFoundException("Account not found with ID: " + id);
        }
        bankAccountRepository.deleteById(id);
    }

    @Override
    public BankAccount updateAccount(Long id, BankAccountRequestDTO request) throws BankAccountNotFoundException {
        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with ID: " + id));

        if (request.getRib() != null && isValidRibFormat(request.getRib())) {
            existingAccount.setRib(request.getRib());
        }

        if (request.getBalance() != null) {
            existingAccount.setBalance(request.getBalance());
        }



        return bankAccountRepository.save(existingAccount);
    }

    @Override
    public BankAccount getAccountByRib(String rib) throws BankAccountNotFoundException {
        return bankAccountRepository.findByRib(rib)
                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found with RIB: " + rib));
    }

    @Override
    public Page<BankAccount> searchAccounts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bankAccountRepository.searchByRibContainingIgnoreCase(keyword, pageable);
    }

}
