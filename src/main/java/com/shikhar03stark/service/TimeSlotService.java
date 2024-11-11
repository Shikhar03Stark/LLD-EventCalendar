package com.shikhar03stark.service;

import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TimeSlotService {
    TimeSlot timeSlotWithDuration(LocalDateTime startTime, Duration duration);
    List<TimeSlot> availableSlotsForUsers(List<User> users, LocalDate date);
    List<TimeSlot> availableSlotsForParticipants(List<User> users, List<Team> teams, LocalDate date);
}
