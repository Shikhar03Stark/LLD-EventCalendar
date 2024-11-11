package com.shikhar03stark.service.impl;

import com.shikhar03stark.model.Event;
import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;
import com.shikhar03stark.service.EventService;
import com.shikhar03stark.service.ParticipantService;
import com.shikhar03stark.service.UserCalendarService;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EventServiceImpl implements EventService {

    private final ParticipantService participantService;
    private final UserCalendarService userCalendarService;

    public EventServiceImpl(ParticipantService participantService, UserCalendarService userCalendarService) {
        this.participantService = participantService;
        this.userCalendarService = userCalendarService;
    }

    private Set<User> getUniqueInvitedUsers(List<User> users, List<Team> teams) {
        final Set<User> uniqueInvitedUsers = new HashSet<>();
        if(Objects.nonNull(teams)) {
            uniqueInvitedUsers.addAll(participantService.resolveUniqueUsers(teams));
        }
        if(Objects.nonNull(users)) {
            uniqueInvitedUsers.addAll(users);
        }
        return uniqueInvitedUsers;
    }

    private TimeSlot getTimeSlotOfEvent(Event event) {
        return new TimeSlot(event.getStartTime(), event.getEndTime());
    }

    @Override
    public Event createDraftEvent(TimeSlot slot, List<User> users, List<Team> teams) {
        final Event event = new Event(slot.getStartTime(), slot.getEndTime());
        if (Objects.nonNull(users)) {
            users.forEach(event::inviteUser);
        }
        if (Objects.nonNull(teams)) {
            teams.forEach(event::inviteTeam);
        }
        return event;
    }

    @Override
    public Event createDraftEvent(TimeSlot slot) {
        return createDraftEvent(slot, null, null);
    }

    @Override
    public boolean allInvitesAvailable(Event event) {
        final Set<User> invitedUsers = getUniqueInvitedUsers(event.getInvitedUsers(), event.getInvitedTeams());
        final TimeSlot slot = getTimeSlotOfEvent(event);
        return invitedUsers.stream()
                .allMatch(user -> userCalendarService.isSlotAvailable(user, slot));
    }

    @Override
    public void blockCalendarForAllInvites(Event event) {
        if (allInvitesAvailable(event)) {
            final Set<User> invites = getUniqueInvitedUsers(event.getInvitedUsers(), event.getInvitedTeams());
            invites.forEach(invite -> userCalendarService.bookSlot(invite, getTimeSlotOfEvent(event)));
            event.confirmInvitation();
        }
    }
}
