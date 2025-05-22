package com.kdev5.cleanpick.review.service.dto.request;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import lombok.Getter;


@Getter
public class WriteReviewRequestDto {
    private Long targetId;
    private Long contractId;
    private Float rating;
    private String content;

    public Review toEntity(Customer customer, Manager manager, Contract contract, ReviewType reviewType) {
        return Review.builder()
                .customer(customer)
                .manager(manager)
                .contract(contract)
                .rating(rating)
                .content(content)
                .type(reviewType)
                .build();
    }
}
