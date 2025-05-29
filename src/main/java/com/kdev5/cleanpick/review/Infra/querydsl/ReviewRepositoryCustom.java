package com.kdev5.cleanpick.review.Infra.querydsl;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    Map<Long, Double> getAvgRatingForMangers(List<Long> managerId);

    Map<Long, Long> getReviewCountForManagers(List<Long> managerId);
}
