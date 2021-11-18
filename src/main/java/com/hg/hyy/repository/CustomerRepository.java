package com.hg.hyy.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.hg.hyy.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);

    Customer findById(long id);
}