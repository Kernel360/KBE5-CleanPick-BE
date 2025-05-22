package com.kdev5.cleanpick.review.service.dto.response;

import com.kdev5.cleanpick.review.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ReviewResponseDto {
    private long id;
    private String targetName;
    private Float rating;
    private String content;
    private List<String> files;


    public static ReviewResponseDto fromEntity(Review review, List<String> files) {

        String targetName = review.getType() == null ? null :
                review.getType().equals(com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER)
                        ? review.getManager().getName()
                        : review.getCustomer().getName();


        return ReviewResponseDto.builder()
                .id(review.getId())
                .targetName(targetName)
                .rating(review.getRating())
                .content(review.getContent())
                .files(files)
                .build();
    }
}