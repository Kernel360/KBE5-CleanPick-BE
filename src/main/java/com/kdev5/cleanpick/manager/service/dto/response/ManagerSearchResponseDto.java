package com.kdev5.cleanpick.manager.service.dto.response;

import java.util.List;

public record ManagerSearchResponseDto(
        Long id,
        String name,
        double averageRating,
        long reviewCount,
        String profileImageUrl,
        String profileMessage,
        String regionSummary,              // 추가
        List<String> cleaningServices
) {

}
