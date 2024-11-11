package com.shikhar03stark.service.impl;

import com.shikhar03stark.Main;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;
import com.shikhar03stark.model.UserCalendar;
import com.shikhar03stark.service.TimeSlotService;
import com.shikhar03stark.service.UserCalendarService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class UserCalendarServiceImpl implements UserCalendarService {

    private List<TimeSlot> getOverlappingBlockedSlotsAt(UserCalendar userCalendar, LocalDateTime time) {
        final List<TimeSlot> overlappingSlots = new ArrayList<>();
        for(TimeSlot blockedSlot: userCalendar.getBlockedSlots()) {
            if (!blockedSlot.getStartTime().isBefore(time)) break;
            if (blockedSlot.getEndTime().isBefore(time)) continue;
            overlappingSlots.add(blockedSlot);
        }
        return overlappingSlots;
    }

    private List<TimeSlot> getBookedSlotsWithStartTimeBetween(UserCalendar userCalendar, LocalDateTime startTime, LocalDateTime endTime) {
        final TimeSlot startSlot = new TimeSlot(startTime, startTime);
        final TimeSlot endSlot = new TimeSlot(endTime, endTime);

        if (userCalendar.getBlockedSlots() instanceof TreeSet<TimeSlot>) {
            return ((TreeSet<TimeSlot>) userCalendar.getBlockedSlots())
                    .subSet(startSlot, true, endSlot, true)
                    .stream()
                    .toList();
        }

        // iterate the set and add slots between startTime and endTime
        final List<TimeSlot> blockedSlots = new ArrayList<>();
        for(TimeSlot blockedSlot: userCalendar.getBlockedSlots()) {
            if (!blockedSlot.getStartTime().isBefore(startTime) && !blockedSlot.getStartTime().isAfter(endTime)) {
                blockedSlots.add(blockedSlot);
            }
        }
        return blockedSlots;
    }

    private boolean isWorkingHourOverlap(User user, TimeSlot slot) {
        if (!slot.getStartDate().isEqual(slot.getEndDate())) return false;
        final LocalDateTime loginTime = slot.getStartDate().atTime(user.getLoginTime());
        final LocalDateTime logoutTime = slot.getEndDate().atTime(user.getLogoutTime());

        if (!slot.getStartTime().isBefore(loginTime) && !slot.getEndTime().isAfter(logoutTime)) {
            return true;
        }
        return false;
    }

    @Override
    public List<TimeSlot> getBookedSlots(User user, TimeSlot slot) {
        if (!isWorkingHourOverlap(user, slot)) return new ArrayList<>();
        // get overlapping slots at StartTime
        final List<TimeSlot> bookedSlots = getOverlappingBlockedSlotsAt(user.getUserCalendar(),slot.getStartTime());

        // add all booked slots starting between startTime and endTime
        bookedSlots
                .addAll(getBookedSlotsWithStartTimeBetween(
                        user.getUserCalendar(),
                        slot.getStartTime(),
                        slot.getEndTime()
                ));

        return bookedSlots;
    }

    @Override
    public boolean isSlotAvailable(User user, TimeSlot slot) {
        if(!isWorkingHourOverlap(user, slot)) return false;
        return getBookedSlots(user, slot).isEmpty();
    }

    @Override
    public void bookSlot(User user, TimeSlot slot) {
        if (!isSlotAvailable(user, slot)) return;
        user.getUserCalendar().blockSlot(slot);
    }
}
