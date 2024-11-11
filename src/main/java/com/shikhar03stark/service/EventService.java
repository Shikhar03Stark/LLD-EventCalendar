package com.shikhar03stark.service;

import com.shikhar03stark.model.Event;
import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event createDraftEvent(TimeSlot slot, List<User> users, List<Team> teams);

    Event createDraftEvent(TimeSlot slot);

    boolean allInvitesAvailable(Event event);

    void blockCalendarForAllInvites(Event event);
}
