package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.review.domain.ReviewFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewFile, Long> {
}
