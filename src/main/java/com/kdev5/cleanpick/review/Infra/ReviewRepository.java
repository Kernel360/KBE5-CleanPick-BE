package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.review.Infra.querydsl.ReviewRepositoryCustom;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    @Query("SELECT r FROM Review r WHERE r.contract = :contract AND r.customer = :customer AND r.manager = :manager AND r.type = :type")
    Optional<Review> findReviewByReviewType(
            @Param("contract") Contract contract,
            @Param("customer") Customer customer,
            @Param("manager") Manager manager,
            @Param("type") ReviewType type
    );

    @EntityGraph(attributePaths = {"manager"})
    @Query("SELECT r FROM Review r WHERE r.customer.id = :customerId AND r.type = 'TO_MANAGER'")
    Page<Review> findAllReviewByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"customer"})
    @Query("SELECT r FROM Review r WHERE r.manager.id = :managerId AND r.type = 'TO_USER'")
    Page<Review> findAllReviewByManagerId(@Param("managerId") Long managerId, Pageable pageable);

    @Query(value = "SELECT r.id FROM review r WHERE r.created_at >= DATE_SUB(NOW(), INTERVAL 1 DAY) ORDER BY r.rating DESC, r.id DESC LIMIT 3", nativeQuery = true)
    List<Long> findTopReviewIds();

    @Query("SELECT DISTINCT r FROM Review r " +
            "JOIN FETCH r.customer " +
            "JOIN FETCH r.manager " +
            "LEFT JOIN FETCH r.reviewFiles " +  // 리뷰 이미지도 fetch join
            "WHERE r.id IN :ids")
    List<Review> findReviewsWithCustomerManagerAndFiles(List<Long> ids);

}
