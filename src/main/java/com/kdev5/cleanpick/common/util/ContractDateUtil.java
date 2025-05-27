package com.kdev5.cleanpick.common.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContractDateUtil {

    // 날짜 계산
    private List<LocalDateTime> generateContractDates(LocalDateTime start, List<DayOfWeek> targetDays, int count) {
        List<LocalDateTime> result = new ArrayList<>();
        LocalDate currentDate = start.toLocalDate();
        int added = 0;

        while (added < count) {
            if (targetDays.contains(currentDate.getDayOfWeek())) {
                result.add(currentDate.atTime(start.toLocalTime()));
                added++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return result;
    }
    
}
