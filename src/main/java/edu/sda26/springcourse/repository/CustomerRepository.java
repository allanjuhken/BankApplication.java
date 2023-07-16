package edu.sda26.springcourse.repository;

import edu.sda26.springcourse.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository <Customer, Long> {

    List<Customer> findAllByNameAndActive(String name, Boolean active);

    @Query(value = "SELECT c FROM Customer c WHERE c.name = :name AND c.active = :status")
    List<Customer> getCustomerByNameAndStatus(String name, Boolean status);
}
