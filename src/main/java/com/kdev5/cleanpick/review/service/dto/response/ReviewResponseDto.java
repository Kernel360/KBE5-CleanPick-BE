package com.kdev5.cleanpick.review.service.dto.response;

import com.kdev5.cleanpick.review.domain.Review;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String targetName;
    private Float rating;
    private String content;
    private List<String> files;

    private ReviewResponseDto(Long id, String targetName, Float rating, String content, List<String> files) {
        this.id = id;
        this.targetName = targetName;
        this.rating = rating;
        this.content = content;
        this.files = files;
    }

    public static ReviewResponseDto fromEntity(Review review, List<String> files) {
        String targetName = review.getType().equals(com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER)
                ? review.getManager().getName()
                : review.getCustomer().getName();

        return new ReviewResponseDto(
                review.getId(),
                targetName,
                review.getRating(),
                review.getContent(),
                files
        );
    }
}