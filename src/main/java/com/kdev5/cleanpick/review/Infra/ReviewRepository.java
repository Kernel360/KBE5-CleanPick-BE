package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
