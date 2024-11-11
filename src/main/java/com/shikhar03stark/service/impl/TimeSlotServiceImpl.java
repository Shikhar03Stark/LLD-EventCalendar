package com.shikhar03stark.service.impl;

import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;
import com.shikhar03stark.service.ParticipantService;
import com.shikhar03stark.service.TimeSlotService;
import com.shikhar03stark.service.UserCalendarService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TimeSlotServiceImpl implements TimeSlotService {

    private final UserCalendarService userCalendarService;
    private final ParticipantService participantService;

    public TimeSlotServiceImpl(UserCalendarService userCalendarService, ParticipantService participantService) {
        this.userCalendarService = userCalendarService;
        this.participantService = participantService;
    }

    private List<TimeSlot> getAvailableTimeSlots(Set<TimeSlot> blockedSlots, TimeSlot daySlot) {
        LocalDateTime endTime = daySlot.getStartDate().atStartOfDay();
        final List<TimeSlot> availableSlots = new ArrayList<>();
        for(TimeSlot blockedSlot: blockedSlots) {
            if (blockedSlot.getEndTime().isBefore(endTime) || blockedSlot.getEndTime().isEqual(endTime)) continue;
            if (blockedSlot.getStartTime().isAfter(endTime)) {
                // endTime to blockedSlot.getStartTime is available slot
                final TimeSlot availableSlot = new TimeSlot(endTime, blockedSlot.getStartTime());
                availableSlots.add(availableSlot);
            }
            // extend the endTime to maximum blockedEndTime
            if (blockedSlot.getEndTime().isAfter(endTime)) {
                endTime = blockedSlot.getEndTime();
            }
        }
        return availableSlots;
    }

    @Override
    public TimeSlot timeSlotWithDuration(LocalDateTime startTime, Duration duration) {
        final LocalDateTime endTime = startTime.plus(duration);
        return new TimeSlot(startTime, endTime);
    }

    // Get list of all booked slots of all users on the date.
    // Count free time from 00:00 to 23:59
    @Override
    public List<TimeSlot> availableSlotsForUsers(List<User> users, LocalDate date) {
        final LocalDateTime startTime = date.atStartOfDay();
        final LocalDateTime endTime = date.atTime(23, 59, 59);
        final TimeSlot daySlot = new TimeSlot(startTime, endTime);
        final Set<TimeSlot> blockedSlots = users.stream()
                .map(user -> userCalendarService.getBookedSlots(user, daySlot))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(TreeSet::new));

        return getAvailableTimeSlots(blockedSlots, daySlot);
    }

    @Override
    public List<TimeSlot> availableSlotsForParticipants(List<User> users, List<Team> teams, LocalDate date) {
        final List<User> resolvedUsers = participantService.resolveUniqueUsers(teams);
        resolvedUsers.addAll(users);
        return availableSlotsForUsers(resolvedUsers, date);
    }

}
