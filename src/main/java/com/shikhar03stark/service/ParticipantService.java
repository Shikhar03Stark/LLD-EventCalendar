package com.shikhar03stark.service;

import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.User;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ParticipantService {

    User registerUser(String alias, LocalTime loginTime, LocalTime logoutTime);
    Team registerTeam(String alias, List<User> users);
    Optional<User> getUserByAlias(String alias);
    Optional<Team> getTeamByAlias(String alias);

    List<User> resolveUniqueUsers(List<Team> teams);
}
