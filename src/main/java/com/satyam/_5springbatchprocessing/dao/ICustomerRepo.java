package com.satyam._5springbatchprocessing.dao;

import com.satyam._5springbatchprocessing.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepo extends JpaRepository<Customer, Integer> {
}
