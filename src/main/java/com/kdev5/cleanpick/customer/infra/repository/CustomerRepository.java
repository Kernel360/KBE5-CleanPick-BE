package com.kdev5.cleanpick.customer.infra.repository;

import com.kdev5.cleanpick.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findById(Long id);

    boolean existsById(Long id);
}
