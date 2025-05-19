package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
