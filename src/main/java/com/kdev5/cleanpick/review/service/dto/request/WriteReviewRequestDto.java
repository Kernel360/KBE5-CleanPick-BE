package com.kdev5.cleanpick.review.service.dto.request;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WriteReviewRequestDto {

    @NotNull(message = "대상 아이디는 필수입니다.")
    private Long targetId;

    @NotNull(message = "계약 아이디는 필수입니다.")
    private Long contractId;

    @NotNull(message = "평점은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = true, message = "평점은 0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", inclusive = true, message = "평점은 5 이하이어야 합니다.")
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
