package com.shikhar03stark.service;

import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCalendarService {
    List<TimeSlot> getBookedSlots(User user, TimeSlot slot);
    boolean isSlotAvailable(User user, TimeSlot slot);
    void bookSlot(User user, TimeSlot slot);
}
