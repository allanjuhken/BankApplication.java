package edu.sda26.springcourse.service;

import edu.sda26.springcourse.dto.CustomerCreationRequestDto;
import edu.sda26.springcourse.dto.CustomerDto;
import edu.sda26.springcourse.dto.CustomerResponseDto;
import edu.sda26.springcourse.dto.CustomerUpdateRequestDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.exception.CustomerNotFoundException;
import edu.sda26.springcourse.model.Customer;
import edu.sda26.springcourse.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDto> getAllCustomers(){
        List<Customer> customers = customerRepository.findAll();
         return toCustomerDto(customers);
    }

    private List<CustomerDto> toCustomerDto(List<Customer> customers) {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        for(Customer customer : customers) {
            customerDtoList.add(toCustomerDto(customer));
        }
        return customerDtoList;
    }

    public Customer getCustomerById(Long id) throws CustomerNotFoundException {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer Not found", 1));
    }

    public List<Customer> findCustomerByNameAndStatus(String name, Boolean status)
            throws CustomerNotFoundException {

        List<Customer> customers = customerRepository
                .findAllByNameAndActive(name, status);

        if(customers.size() < 1)
            throw new CustomerNotFoundException("Customer Not found", 1);

        return customers;
        // todo replace with better code in next sessions
        // 1. using repository method
        // 2. using Query annotation

        //1. using repository method
        //select c1_0.id,c1_0.active,c1_0.age,c1_0.email,c1_0.name,c1_0.phone from customer
        // c1_0 where c1_0.name=? and c1_0.active=?



        //2. Using @Query annotation
//        return customerRepository.getCustomerByNameAndStatus(name, status);



//        return customerRepository.findAll()
//                .stream()
//                .filter(customer -> customer.getName().equalsIgnoreCase(name))
//                .filter(customer -> customer.getActive().equals(status))
//                .collect(Collectors.toList());

        //select c1_0.id,c1_0.active,c1_0.age,c1_0.email,c1_0.name,c1_0.phone from customer c1_0
    }

    public CustomerResponseDto save(
            CustomerCreationRequestDto customerCreationRequestDto) {
        Customer customer = toCustomer(customerCreationRequestDto);

        //Saved customer object
        Customer savedCustomer = customerRepository.save(customer);

        //Creating CustomerResponseDto Object and getting the value
        // of saved customer and setting into CustomerResponseDto
        //Return CustomerResponseDto
        return toCustomerResponseDto(savedCustomer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Customer save(Customer customer) {
       return customerRepository.save(customer);
    }


    public CustomerDto updateCustomerData(Long id,
                                          CustomerUpdateRequestDto customerUpdateRequestDto) {
        Customer customer = toCustomer(id, customerUpdateRequestDto);

        Customer updatedCustomer = customerRepository.save(customer);

        return toCustomerDto(updatedCustomer);
    }

    public CustomerDto updateCustomerStatus(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        CustomerDto customerDto = null;
        if (customer != null) {
            customer.setActive(true);
            customerDto = toCustomerDto(customerRepository.save(customer));
        }
        return customerDto;
    }


    private CustomerDto toCustomerDto(Customer updatedCustomer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setActive(updatedCustomer.getActive());
        customerDto.setAge(updatedCustomer.getAge());
        customerDto.setId(updatedCustomer.getId());
        customerDto.setPhone(updatedCustomer.getPhone());
        customerDto.setName(updatedCustomer.getName());
        customerDto.setEmail(updatedCustomer.getEmail());
        return customerDto;
    }

    private Customer toCustomer(Long id,
                                CustomerUpdateRequestDto customerUpdateRequestDto) {
        return Customer.builder()
                .age(customerUpdateRequestDto.getAge())
                .email(customerUpdateRequestDto.getEmail())
                .phone(customerUpdateRequestDto.getPhone())
                .active(customerUpdateRequestDto.getActive())
                .name(customerUpdateRequestDto.getName())
                .id(id)
                .build();
    }

    private Customer toCustomer(CustomerCreationRequestDto customerCreationRequestDto) {
        return Customer.builder()
                .age(customerCreationRequestDto.getAge())
                .name(customerCreationRequestDto.getName())
                .email(customerCreationRequestDto.getEmail())
                .phone(customerCreationRequestDto.getPhone())
                .active(true)
                .build();
    }

    private CustomerResponseDto toCustomerResponseDto(Customer savedCustomer) {
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setName(savedCustomer.getName());
        customerResponseDto.setId(savedCustomer.getId());
        return customerResponseDto;
    }


}
