package ma.cigma.service;

import ma.cigma.dto.CustomerDTO;
import ma.cigma.exception.CustomerNotFoundException;

import java.util.List;

public interface CustomerService {
    CustomerDTO addNewCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException, CustomerNotFoundException;


    CustomerDTO updateCustomer(long customerId, CustomerDTO customerDTO) throws CustomerNotFoundException;

    void deleteCustomer(long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> getAllCustomers();

    List<CustomerDTO> searchCustomers(String keyword);
}
