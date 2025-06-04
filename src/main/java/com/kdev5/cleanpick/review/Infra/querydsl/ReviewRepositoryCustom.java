package com.kdev5.cleanpick.review.Infra.querydsl;

import com.kdev5.cleanpick.manager.service.dto.response.ReadManagerResponseDto;

import java.util.List;
import java.util.Map;

public interface ReviewRepositoryCustom {

    Map<Long, Double> getAvgRatingForMangers(List<Long> managerId);

    Map<Long, Long> getReviewCountForManagers(List<Long> managerId);

    List<ReadManagerResponseDto> findManagerRatingsWithInfo(List<Long> managerIds);
}
