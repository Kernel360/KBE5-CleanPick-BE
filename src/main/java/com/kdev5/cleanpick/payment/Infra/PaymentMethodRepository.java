package com.kdev5.cleanpick.payment.Infra;

import com.kdev5.cleanpick.payment.domain.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
