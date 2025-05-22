package com.kdev5.cleanpick.review.domain;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import com.kdev5.cleanpick.review.service.dto.response.ReviewResponseDto;
import jakarta.persistence.*;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private float rating;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewType type;

    @Builder
    public Review(Customer customer, Manager manager, Contract contract, float rating, String content, ReviewType type) {
        this.customer = customer;
        this.manager = manager;
        this.contract = contract;
        this.rating = rating;
        this.content = content;
        this.type = type;
    }

    public ReviewResponseDto toDto(){
        String name = type.equals(ReviewType.TO_MANAGER) ? manager.getName() : customer.getName();

        return ReviewResponseDto.builder()
                .id(id)
                .targetName(name)
                .rating(rating)
                .content(content)
                .build();
    }
}