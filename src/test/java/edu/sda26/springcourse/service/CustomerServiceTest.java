package edu.sda26.springcourse.service;
import edu.sda26.springcourse.dto.CustomerDto;
import edu.sda26.springcourse.exception.AccountNotFoundException;
import edu.sda26.springcourse.exception.CustomerNotFoundException;
import edu.sda26.springcourse.model.Account;
import edu.sda26.springcourse.model.Customer;
import edu.sda26.springcourse.repository.CustomerRepository;
import edu.sda26.springcourse.service.CustomerService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    CustomerRepository customerRepository;

    @Autowired
    CustomerService customerService;

    @Test
    //customerService / getAllcustomers()
    public void testGetAllCustomers_ResponseSuccessfully(){

        //given
        Customer customer1 =new Customer(1L,"Test",25,"3215460","test@tets1.ee",true,"");
        Customer customer2 =new Customer(2L,"Test1",26,"325815460","test1@tets1.ee",true,"");

        when(customerRepository.findAll()).thenReturn(List.of(customer1,customer2));
        CustomerService customerService  = new CustomerService(customerRepository);

        //when
        List<CustomerDto> customers = customerService.getAllCustomers();

        //then
        assert customers.size()==2;
        Assertions.assertTrue(customers.get(0).getActive());
        Assertions.assertEquals(customers.get(0).getEmail(), "test@tets1.ee");
        Assertions.assertEquals(customers.get(0).getName(),"Test");
        Assertions.assertEquals(customers.get(0).getId(),1L);

        Assertions.assertTrue(customers.get(1).getActive());
        Assertions.assertEquals(customers.get(1).getAge(),26);
        Assertions.assertEquals(customers.get(1).getName(),"Test1");
        Assertions.assertEquals(customers.get(1).getId(),2L);
    }

    @Test
    public void testFindCustomerById_returnSuccessful() throws CustomerNotFoundException {
        //given
        Customer customer = new Customer(1L,"Jonni",25,"321","testJonni@tets1.ee",true,"");
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        CustomerService customerService = new CustomerService(customerRepository);

        //when
        Customer selectedCustomer = customerService.getCustomerById(1L);

        //then
        assertEquals(1L,selectedCustomer.getId());
        assertEquals("Jonni",selectedCustomer.getName());
        assertEquals(25, selectedCustomer.getAge());
        assertEquals("321",selectedCustomer.getPhone());
        assertEquals("testJonni@tets1.ee",selectedCustomer.getEmail());
        assertTrue(selectedCustomer.getActive());
    }
    //Lets go through customer Service class and start writing test for different methods
    @Test
    public void testGetCustomerByID_exceptionExpected() throws CustomerNotFoundException{
        //given
        CustomerService customerService = new CustomerService(customerRepository);

        //when and then
        assertThrows(CustomerNotFoundException.class,() ->customerService.getCustomerById(anyLong()));
        verify(customerRepository).findById(anyLong());
    }


}
