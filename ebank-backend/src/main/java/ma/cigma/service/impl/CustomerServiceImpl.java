package ma.cigma.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.CustomerDTO;
import ma.cigma.entity.BankAccount;
import ma.cigma.entity.Customer;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.repository.BankAccountRepository;
import ma.cigma.repository.CustomerRepository;
import ma.cigma.service.CustomerService;
import ma.cigma.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private EmailService emailService;
    private ModelMapper modelMapper;
    @Override
    public CustomerDTO addNewCustomer(CustomerDTO customerDTO) {
        // Convertir CustomerDTO en Customer
        Customer customer = modelMapper.map(customerDTO, Customer.class);

        // RG_4: Le numéro d’identité doit être unique
        if (customerRepository.findByCin(customer.getCin()).isPresent()) {
            throw new IllegalArgumentException("CIN must be unique");
        }
        // RG_6: L’adresse mail doit être unique
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email must be unique");
        }
        // Sauvegarde du client
        Customer savedCustomer = customerRepository.save(customer);
        // RG_7: Envoyer un email avec le login et le mot de passe
        String login = generateLogin(savedCustomer);
        String password = generatePassword();
        emailService.sendEmail(savedCustomer.getEmail(), "Welcome", "Your login: " + login + " and password: " + password);
        // Retourner le CustomerDTO
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    private String generateLogin(Customer customer) {
        return customer.getEmail();
    }

    private String generatePassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "").substring(0, 10); // Génère un mot de passe de 10 caractères aléatoires
    }


    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(long customerId, CustomerDTO customerDTO) throws CustomerNotFoundException {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerId + " not found"));

        existingCustomer.setName(customerDTO.getName());
        existingCustomer.setCin(customerDTO.getCin());
        existingCustomer.setEmail(customerDTO.getEmail());
        existingCustomer.setBirthday(customerDTO.getBirthday());
        existingCustomer.setPostalAddress(customerDTO.getPostalAddress());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(long customerId) throws CustomerNotFoundException {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customerId + " not found"));

        // Supprimer tous les comptes bancaires associés
        List<BankAccount> bankAccounts = bankAccountRepository.findByCustomerId(customerId);
        for (BankAccount bankAccount : bankAccounts) {
            bankAccountRepository.delete(bankAccount);
        }

        // Ensuite, supprimer le client
        customerRepository.delete(existingCustomer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customerList = customerRepository.findByNameContains(keyword);
        return customerList.stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

}
