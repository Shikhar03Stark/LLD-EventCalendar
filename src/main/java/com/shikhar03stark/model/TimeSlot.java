package com.shikhar03stark.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlot implements Comparable<TimeSlot> {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if(startTime.isAfter(endTime)) throw new RuntimeException("startTime should come before endTime");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDate getStartDate() {
        return startTime.toLocalDate();
    }

    public LocalDate getEndDate() {
        return endTime.toLocalDate();
    }

    public LocalTime getStartTimeOfDay() {
        return startTime.toLocalTime();
    }

    public LocalTime getEndTimeOfDay() {
        return  endTime.toLocalTime();
    }

    @Override
    public int compareTo(TimeSlot other) {
        return this.startTime.isEqual(other.startTime)
                ? this.endTime.compareTo(other.endTime)
                : this.startTime.compareTo(other.startTime);

    }
}
