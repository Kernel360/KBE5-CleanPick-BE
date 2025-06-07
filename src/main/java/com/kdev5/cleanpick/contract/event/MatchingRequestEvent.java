package com.kdev5.cleanpick.contract.event;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MatchingRequestEvent {
    private final Long contractId;
    private final double latitude;
    private final double longitude;
    private final List<LocalDateTime> contractDates; // 루틴용
    private final LocalDateTime start; // 단일용
    private final LocalDateTime end;   // 단일용

    public static MatchingRequestEvent forRoutine(Long contractId, double lat, double lon, List<LocalDateTime> dates) {
        return MatchingRequestEvent.builder()
                .contractId(contractId)
                .latitude(lat)
                .longitude(lon)
                .contractDates(dates)
                .build();
    }

    public static MatchingRequestEvent forSingle(Long contractId, double lat, double lon, LocalDateTime start, LocalDateTime end) {
        return MatchingRequestEvent.builder()
                .contractId(contractId)
                .latitude(lat)
                .longitude(lon)
                .start(start)
                .end(end)
                .build();
    }
}

