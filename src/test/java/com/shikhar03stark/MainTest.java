package com.shikhar03stark;

import com.shikhar03stark.model.Event;
import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.TimeSlot;
import com.shikhar03stark.model.User;
import com.shikhar03stark.service.UserCalendarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MainTest {

    private final ServiceProvider serviceProvider = new ServiceProvider();

    @BeforeEach
    public void beforeAll() {
        serviceProvider.resetContext();
    }

    @Test
    public void RegisterUsersAndTeamTest() {
        final User user1 = serviceProvider.getParticipantService().registerUser("hvish", LocalTime.of(8, 0), LocalTime.of(19, 0));
        final User user2 = serviceProvider.getParticipantService().registerUser("shruty", LocalTime.of(8, 0), LocalTime.of(16, 0));
        final User user3 = serviceProvider.getParticipantService().registerUser("dubu", LocalTime.of(11, 0), LocalTime.of(15, 0));

        final Team teamFem = serviceProvider.getParticipantService().registerTeam("fem", List.of(user2, user3));

        Assertions.assertEquals(List.of(user2, user3), teamFem.getUsers());
    }

    @Test
    public void CreateEventAndBlockUsersTest() {
        final User user1 = serviceProvider.getParticipantService().registerUser("hvish", LocalTime.of(8, 0), LocalTime.of(19, 0));
        final User user2 = serviceProvider.getParticipantService().registerUser("shruty", LocalTime.of(8, 0), LocalTime.of(16, 0));
        final User user3 = serviceProvider.getParticipantService().registerUser("dubu", LocalTime.of(11, 0), LocalTime.of(15, 0));

        final Team teamFem = serviceProvider.getParticipantService().registerTeam("fem", List.of(user2, user3));

        final TimeSlot standupTimeSlot = serviceProvider.getTimeSlotService().timeSlotWithDuration(LocalDate.now().atTime(12, 0), Duration.of(1, ChronoUnit.HOURS));
        final Event standupMeeting = serviceProvider.getEventService().createDraftEvent(standupTimeSlot);
        standupMeeting.inviteUser(user1);
        standupMeeting.inviteTeam(teamFem);

        Assertions.assertTrue(serviceProvider.getEventService().allInvitesAvailable(standupMeeting));

        serviceProvider.getEventService().blockCalendarForAllInvites(standupMeeting);

        Assertions.assertTrue(standupMeeting.isConfirmed());

        final List<TimeSlot> user1BlockedSlots = user1.getUserCalendar().getBlockedSlots().stream().toList();
        Assertions.assertEquals(1, user1BlockedSlots.size());
        Assertions.assertEquals(1, user2.getUserCalendar().getBlockedSlots().size());
        Assertions.assertEquals(1, user3.getUserCalendar().getBlockedSlots().size());

        Assertions.assertEquals(LocalDate.now().atTime(12, 0), user1BlockedSlots.get(0).getStartTime());
        Assertions.assertEquals(LocalDate.now().atTime(13, 0), user1BlockedSlots.get(0).getEndTime());
    }

    @Test
    public void ConflictingEventTest() {
        final User user1 = serviceProvider.getParticipantService().registerUser("hvish", LocalTime.of(8, 0), LocalTime.of(19, 0));
        final User user2 = serviceProvider.getParticipantService().registerUser("shruty", LocalTime.of(8, 0), LocalTime.of(16, 0));
        final User user3 = serviceProvider.getParticipantService().registerUser("dubu", LocalTime.of(11, 0), LocalTime.of(15, 0));

        final Team team = serviceProvider.getParticipantService().registerTeam("fem", List.of(user2, user3));

        final TimeSlot teamSlot = serviceProvider.getTimeSlotService().timeSlotWithDuration(LocalDate.now().atTime(13, 0), Duration.of(45, ChronoUnit.MINUTES));
        final Event teamEvent = serviceProvider.getEventService().createDraftEvent(teamSlot);
        teamEvent.inviteTeam(team);
        serviceProvider.getEventService().blockCalendarForAllInvites(teamEvent);
        Assertions.assertTrue(teamEvent.isConfirmed());

        final TimeSlot userSlot = serviceProvider.getTimeSlotService().timeSlotWithDuration(LocalDate.now().atTime(12, 30), Duration.of(60, ChronoUnit.MINUTES));
        final Event userEvent = serviceProvider.getEventService().createDraftEvent(userSlot);
        userEvent.inviteUser(user1);
        userEvent.inviteUser(user2);

        Assertions.assertFalse(serviceProvider.getEventService().allInvitesAvailable(userEvent));
    }

    @Test
    public void AvailableSlotSuggestionTest() {
        final User user1 = serviceProvider.getParticipantService().registerUser("hvish", LocalTime.of(8, 0), LocalTime.of(19, 0));
        final User user2 = serviceProvider.getParticipantService().registerUser("shruty", LocalTime.of(8, 0), LocalTime.of(16, 0));
        final User user3 = serviceProvider.getParticipantService().registerUser("dubu", LocalTime.of(11, 0), LocalTime.of(15, 0));

        final Team team = serviceProvider.getParticipantService().registerTeam("fem", List.of(user2, user3));

        final TimeSlot teamSlot = serviceProvider.getTimeSlotService().timeSlotWithDuration(LocalDate.now().atTime(13, 0), Duration.of(45, ChronoUnit.MINUTES));
        final Event teamEvent = serviceProvider.getEventService().createDraftEvent(teamSlot);
        teamEvent.inviteTeam(team);
        serviceProvider.getEventService().blockCalendarForAllInvites(teamEvent);
        Assertions.assertTrue(teamEvent.isConfirmed());

        final List<TimeSlot> availableSuggestion = serviceProvider.getTimeSlotService().availableSlotsForUsers(List.of(user1, user2, user3), LocalDate.now());
        Assertions.assertEquals(2, availableSuggestion.size());
    }
  
}