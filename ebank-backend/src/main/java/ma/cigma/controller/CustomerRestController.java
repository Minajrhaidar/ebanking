package ma.cigma.controller;


import ma.cigma.dto.*;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin("*")
public class CustomerRestController {


    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
    }

    @PostMapping
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.addNewCustomer(customerDTO);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("{id}")
    public CustomerDTO getCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {

        return customerService.getCustomer(customerId);
    }

    @PutMapping("{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable(name = "id") long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException {
        CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}