package ma.cigma.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.cigma.dto.CustomerDTO;
import ma.cigma.entity.Customer;
import ma.cigma.repository.CustomerRepository;
import ma.cigma.service.CustomerService;
import ma.cigma.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {


    private CustomerRepository customerRepository;
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
        return UUID.randomUUID().toString();
    }





//
//    @Override
//    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
//        log.info("Saving new customer");
//        Customer customer = modelMapper.map(customerDTO, Customer.class);
//        Customer savedCustomer = customerRepository.save(customer);
//        return modelMapper.map(savedCustomer, CustomerDTO.class);
//    }
//    @Override
//    public List<CustomerDTO> listCustomers() {
//        return customerRepository.findAll().stream()
//                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
//                .collect(Collectors.toList());
//    }
//    @Override
//    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
//
//        return modelMapper.map(customer, CustomerDTO.class);
//    }
//
//    @Override
//    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
//        log.info("Updating customer");
//        Customer customer = modelMapper.map(customerDTO, Customer.class);
//        Customer savedCustomer = customerRepository.save(customer);
//        return modelMapper.map(savedCustomer, CustomerDTO.class);
//    }
//
//    @Override
//    public void deleteCustomer(Long customerId) {
//        customerRepository.deleteById(customerId);
//    }
//
//
//    @Override
//    public List<CustomerDTO> searchCustomers(String keyword) {
//        return customerRepository.findByName(keyword).stream()
//                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
//                .collect(Collectors.toList());
//    }

}
