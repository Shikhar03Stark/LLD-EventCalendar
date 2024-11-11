package com.shikhar03stark.model;

import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;

public class UserCalendar {
    private final LocalTime loginTime;
    private final LocalTime logoutTime;
    private final Set<TimeSlot> blockedSlots;

    public UserCalendar(LocalTime loginTime, LocalTime logoutTime) {
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        blockedSlots = new TreeSet<>();
    }

    public LocalTime getLoginTime() {
        return loginTime;
    }

    public LocalTime getLogoutTime() {
        return logoutTime;
    }

    public Set<TimeSlot> getBlockedSlots() {
        return this.blockedSlots;
    }

    public void blockSlot(TimeSlot slot) {
        blockedSlots.add(slot);
    }

}
