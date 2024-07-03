package ma.cigma.controller;


import ma.cigma.dto.*;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agent/customers")
@CrossOrigin("*")
public class CustomerRestController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerRestController.class);
    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    public CustomerDTO createCustomer(@RequestBody CustomerDTO customerDTO) {
        logger.info("Creating new customer");
        return customerService.addNewCustomer(customerDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword) {
        List<CustomerDTO> customers = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_AGENT_GUICHET', 'ROLE_CLIENT')")
    public CustomerDTO getCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {

        return customerService.getCustomer(customerId);
    }

    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    @PutMapping("{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(name = "id") long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_AGENT_GUICHET')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
