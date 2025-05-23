package com.kdev5.cleanpick.review.Infra;

import com.kdev5.cleanpick.review.domain.Review;
import com.kdev5.cleanpick.review.domain.ReviewFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewFileRepository extends JpaRepository<ReviewFile, Long> {

    @Query("Select rf.reviewFileUrl From ReviewFile rf where rf.review =:review")
    List<String> findReviewFileByReview(Review review);
}
