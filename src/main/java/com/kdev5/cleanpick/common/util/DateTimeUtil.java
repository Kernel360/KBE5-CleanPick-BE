package com.kdev5.cleanpick.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateTimeUtil {

    public static class DateTimeParts {
        private final LocalDate date;
        private final LocalTime time;

        public DateTimeParts(LocalDate date, LocalTime time) {
            this.date = date;
            this.time = time;
        }

        public LocalDate getDate() {
            return date;
        }

        public LocalTime getTime() {
            return time;
        }
    }

    public static DateTimeParts splitDateTime(LocalDateTime dateTime) {
        return new DateTimeParts(dateTime.toLocalDate(), dateTime.toLocalTime());
    }
}