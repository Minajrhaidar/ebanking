package ma.cigma.controller;


import ma.cigma.dto.*;
import ma.cigma.entity.Customer;
import ma.cigma.exception.CustomerNotFoundException;
import ma.cigma.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin("*")
public class CustomerRestController {


    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerRestController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public CustomerDTO addCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.addNewCustomer(customerDTO);
    }



//
//    @GetMapping
//    public List<CustomerDTO> customers(){
//
//        return customerService.listCustomers();
//    }
//    @GetMapping("search")
//    public List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword ){
//
//        return customerService.searchCustomers(keyword);
//    }
//    @GetMapping("{id}")
//    public  CustomerDTO getCustomer(@PathVariable(name = "id") long customerId) throws CustomerNotFoundException {
//
//        return customerService. getCustomer(customerId);
//    }
//
//    @PostMapping
//    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
//
//
//        return  customerService.saveCustomer(customerDTO);
//
//    }
//    @PutMapping("{customerId}")
//
//    public CustomerDTO updatedCustumer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO){
//
//        customerDTO.setId(customerId);
//        return  customerService.updateCustomer(customerDTO);
//
//
//    }
//    @DeleteMapping("{id}")
//    public void deleteCustomer(@PathVariable Long id ){
//        customerService.deleteCustomer(id);
//
//    }
}