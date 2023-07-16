package edu.sda26.springcourse.controller;

import edu.sda26.springcourse.dto.CustomerCreationRequestDto;
import edu.sda26.springcourse.dto.CustomerDto;
import edu.sda26.springcourse.dto.CustomerResponseDto;
import edu.sda26.springcourse.dto.CustomerUpdateRequestDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.exception.CustomerNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.Customer;
import edu.sda26.springcourse.service.AccountService;
import edu.sda26.springcourse.service.CustomerService;
//import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final AccountService accountService;

    public CustomerController(CustomerService customerService,
                              AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping(path = "/customers")
    public List<CustomerDto> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping(path = "/customer/{id}")
    public ResponseEntity<Object> getCustomerById(
            @PathVariable("id") Long id) {
         Customer customer;
         try {
             customer = customerService.getCustomerById(id);
         } catch (CustomerNotFoundException e) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body("Error Message: "
                             + e.getMessage() +
                             " - Error Code: " +
                             e.getErrorCode());
         }
         return ResponseEntity.ok(customer);
    }

//    @GetMapping(path = "/customer/{name}")
//    public ResponseEntity<List<Customer>> getCustomerByName(@PathVariable("name") String name) {
//        List<Customer> customers = customerService.findCustomerByName(name);
//
//        if (customers.size() < 1)
//            return ResponseEntity.notFound().build();
//
//        return ResponseEntity.ok(customers);
//    }

    @GetMapping(path = "/customer")
    public ResponseEntity<List<Customer>> getCustomerByNameAndStatus(
            @RequestParam("name") String name,
            @RequestParam("status") Boolean status) throws CustomerNotFoundException {
        List<Customer> customers =
                customerService.findCustomerByNameAndStatus(name, status);
        return ResponseEntity.ok(customers);
    }


    @GetMapping(path = "/customer/{id}/account")
    public ResponseEntity<Account> getAccountOfCustomer(@PathVariable("id") Long customerId) throws AccountNotFoundException {
        Account account =
                accountService.findAccountByCustomerId(customerId);

        if (account == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(account);
    }

    @PutMapping(path = "/customer/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable("id") Long id,
            @RequestBody CustomerUpdateRequestDto customerUpdateRequestDto) {
        return ResponseEntity
                .accepted()
                .body(customerService.updateCustomerData(id,
                        customerUpdateRequestDto));
    }

    // Sometimes RequestDto and ResponseDto can be same class
    // And sometimes has to be different
    // it depends on the api that you are implementing


    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(path = "/customer")
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerCreationRequestDto customerCreationRequestDto) {

        CustomerResponseDto savedCustomer =
                customerService.save(customerCreationRequestDto);

        return ResponseEntity.ok(savedCustomer);
    }

    @DeleteMapping(path = "/customer/{id}")
    public ResponseEntity removeCustomer(@PathVariable("id") Long id) {
        customerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/customer/{id}/activate")
    public ResponseEntity<CustomerDto> activateCustomer(
            @PathVariable("id") Long id) {
        return ResponseEntity.accepted().body(
                customerService.updateCustomerStatus(id));
    }

}
