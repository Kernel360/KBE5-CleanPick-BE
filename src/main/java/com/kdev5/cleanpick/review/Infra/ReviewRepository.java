package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.contract = :contract AND r.customer = :customer AND r.manager = :manager AND r.type = :type")
    Optional<Review> findReviewByReviewType(
            @Param("contract") Contract contract,
            @Param("customer") Customer customer,
            @Param("manager") Manager manager,
            @Param("type") ReviewType type
    );

}
