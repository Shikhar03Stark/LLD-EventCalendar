package com.shikhar03stark.model;

import java.time.LocalTime;

public class User extends Participant {

    private final UserCalendar userCalendar;

    public User(String alias, LocalTime loginTime, LocalTime logoutTime) {
        super(alias, "microsoft.com");
        userCalendar = new UserCalendar(loginTime, logoutTime);
    }

    public LocalTime getLoginTime() {
        return userCalendar.getLoginTime();
    }

    public LocalTime getLogoutTime() {
        return userCalendar.getLogoutTime();
    }

    public UserCalendar getUserCalendar() {
        return userCalendar;
    }

}
