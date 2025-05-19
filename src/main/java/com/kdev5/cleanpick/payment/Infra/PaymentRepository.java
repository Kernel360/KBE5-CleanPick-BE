package com.kdev5.cleanpick.payment.Infra;

import com.kdev5.cleanpick.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
