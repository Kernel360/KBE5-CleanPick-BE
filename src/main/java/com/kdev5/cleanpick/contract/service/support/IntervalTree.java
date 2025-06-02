package com.kdev5.cleanpick.contract.service.support;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class IntervalTree {

    private final Map<Long, TreeSet<TimeInterval>> cleanerSchedule = new ConcurrentHashMap<>();

    public void addReservation(Long cleanerId, TimeInterval interval) {
        cleanerSchedule.computeIfAbsent(cleanerId, k -> new TreeSet<>()).add(interval);
    }

    public boolean isAvailable(Long cleanerId, TimeInterval desired) {
        TreeSet<TimeInterval> intervals = cleanerSchedule.getOrDefault(cleanerId, new TreeSet<>());
        return intervals.stream().noneMatch(existing -> existing.overlaps(desired));
    }

    public void removeReservation(Long cleanerId, TimeInterval timeInterval) {
        TreeSet<TimeInterval> intervals = cleanerSchedule.get(cleanerId);
        if (intervals != null) {
            intervals.remove(timeInterval);
            if (intervals.isEmpty()) {
                cleanerSchedule.remove(cleanerId);
            }
        }
    }

    public void updateReservation(Long cleanerId, TimeInterval oldInterval, TimeInterval newInterval) {
        removeReservation(cleanerId, oldInterval);
        addReservation(cleanerId, newInterval);
    }
}
