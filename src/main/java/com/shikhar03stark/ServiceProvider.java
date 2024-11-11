package com.shikhar03stark;

import com.shikhar03stark.service.EventService;
import com.shikhar03stark.service.ParticipantService;
import com.shikhar03stark.service.TimeSlotService;
import com.shikhar03stark.service.UserCalendarService;
import com.shikhar03stark.service.impl.EventServiceImpl;
import com.shikhar03stark.service.impl.ParticipantServiceImpl;
import com.shikhar03stark.service.impl.TimeSlotServiceImpl;
import com.shikhar03stark.service.impl.UserCalendarServiceImpl;

public class ServiceProvider {

    private EventService eventService;
    private ParticipantService participantService;
    private TimeSlotService timeSlotService;
    private UserCalendarService userCalendarService;

    public ServiceProvider() {
        buildContext();
    }

    private void buildContext() {
        participantService = new ParticipantServiceImpl();
        userCalendarService = new UserCalendarServiceImpl();
        timeSlotService = new TimeSlotServiceImpl(userCalendarService, participantService);
        eventService = new EventServiceImpl(participantService, userCalendarService);
    }

    public void resetContext() {
        buildContext();
    }

    public EventService getEventService() {
        return eventService;
    }

    public ParticipantService getParticipantService() {
        return participantService;
    }

    public TimeSlotService getTimeSlotService() {
        return timeSlotService;
    }

    public UserCalendarService getUserCalendarService() {
        return userCalendarService;
    }
}
