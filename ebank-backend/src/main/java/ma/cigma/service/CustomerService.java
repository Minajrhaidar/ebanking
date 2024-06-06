package ma.cigma.service;

import ma.cigma.dto.CustomerDTO;

public interface CustomerService {
    CustomerDTO addNewCustomer(CustomerDTO customerDTO);


//    CustomerDTO saveCustomer(CustomerDTO customerDTO);
//
//    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
//
//    CustomerDTO updateCustomer(CustomerDTO customerDTO);
//
//    void deleteCustomer(Long customerId);
//
//    List<CustomerDTO> searchCustomers(String keyword);
//    List<CustomerDTO> listCustomers();
}
