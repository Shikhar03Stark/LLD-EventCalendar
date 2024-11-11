package com.shikhar03stark.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private boolean isConfirmed;
    private final List<User> invitedUsers;
    private final List<Team> invitedTeams;

    public Event(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.invitedUsers = new ArrayList<>();
        this.invitedTeams = new ArrayList<>();
        isConfirmed = false;
    }
    
    public void confirmInvitation() {
        isConfirmed = true;
    }

    public Event inviteUser(User user) {
        this.invitedUsers.add(user);
        return this;
    }

    public Event inviteTeam(Team team) {
        this.invitedTeams.add(team);
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public List<User> getInvitedUsers() {
        return invitedUsers;
    }

    public List<Team> getInvitedTeams() {
        return invitedTeams;
    }
}
