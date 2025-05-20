package com.kdev5.cleanpick.customer.infra.repository;

import com.kdev5.cleanpick.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.beans.JavaBean;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
