package ma.cigma.controller;


import ma.cigma.dto.*;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agent/customers")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('CLIENT','AGENT_GUICHET')")
public class CustomerRestController {
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CUSTOMER')")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.addNewCustomer(customerDTO);
    }

    @GetMapping
   @PreAuthorize("hasAuthority('GET_ALL_CUSTUMERS')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('GET_CUSTOMER_BY_IDENTITY')")
    public CustomerDTO getCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {

        return customerService.getCustomer(customerId);
    }

    @PreAuthorize("hasAuthority('UPDATE_CUSTOMER')")
    @PutMapping("{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(name = "id") long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('DELETE_CUSTOMER')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
