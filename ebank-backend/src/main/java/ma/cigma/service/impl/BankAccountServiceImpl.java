package ma.cigma.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.*;
import ma.cigma.entity.*;
import ma.cigma.entity.enums.AccountStatus;
import ma.cigma.entity.enums.OperationType;
import ma.cigma.exception.BakaccountNotFoundException;
import ma.cigma.exception.BalanceNotSufficentException;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.repository.AccountOperationRepository;
import ma.cigma.repository.BankAccountRepository;
import ma.cigma.repository.CustomerRepository;
import ma.cigma.service.BankAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AccountOperationDTO transfer(TransferRequestDTO transferRequestDTO) {
        BankAccount senderAccount = null;
        try {
            senderAccount = bankAccountRepository.findByRib(transferRequestDTO.getSourceRib())
                    .orElseThrow(() -> new BakaccountNotFoundException("Sender bank account not found"));
        } catch (BakaccountNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (senderAccount.getStatus() != AccountStatus.OPEN) {
            throw new IllegalStateException("Sender bank account is not open");
        }

        if (senderAccount.getBalance() < transferRequestDTO.getAmount()) {
            try {
                throw new BalanceNotSufficentException("Insufficient balance in sender bank account");
            } catch (BalanceNotSufficentException e) {
                throw new RuntimeException(e);
            }
        }

        double amount = transferRequestDTO.getAmount();
        String description = transferRequestDTO.getDescription();

        // Débiter le montant du compte expéditeur
        double newSenderBalance = senderAccount.getBalance() - amount;
        senderAccount.setBalance(newSenderBalance);
        bankAccountRepository.save(senderAccount);

        // Enregistrer l'opération de retrait
        AccountOperation withdrawal = new AccountOperation(OperationType.WITHDRAWAL, amount, senderAccount, description);
        accountOperationRepository.save(withdrawal);

        // Créer le compte du destinataire et créditer le montant
        BankAccount receiverAccount = null;
        try {
            receiverAccount = bankAccountRepository.findByRib(transferRequestDTO.getDestinationRib())
                    .orElseThrow(() -> new BakaccountNotFoundException("Receiver bank account not found"));
        } catch (BakaccountNotFoundException e) {
            throw new RuntimeException(e);
        }

        double newReceiverBalance = receiverAccount.getBalance() + amount;
        receiverAccount.setBalance(newReceiverBalance);
        bankAccountRepository.save(receiverAccount);

        // Enregistrer l'opération de dépôt
        AccountOperation deposit = new AccountOperation(OperationType.DEPOSIT, amount, receiverAccount, description);
        accountOperationRepository.save(deposit);

        // Retourner les détails de l'opération de transfert
        return modelMapper.map(withdrawal, AccountOperationDTO.class);
    }


}