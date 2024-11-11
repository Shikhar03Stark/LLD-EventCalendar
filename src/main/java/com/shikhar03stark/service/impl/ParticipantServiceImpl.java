package com.shikhar03stark.service.impl;

import com.shikhar03stark.model.Team;
import com.shikhar03stark.model.User;
import com.shikhar03stark.service.ParticipantService;

import java.time.LocalTime;
import java.util.*;

public class ParticipantServiceImpl implements ParticipantService {

    private final Map<String, User> aliasToUserMap = new HashMap<>();
    private final Map<String, Team> aliasToTeamMap = new HashMap<>();


    @Override
    public User registerUser(String alias, LocalTime loginTime, LocalTime logoutTime) {
        if(!aliasToUserMap.containsKey(alias)) {
            final User user = new User(alias, loginTime, logoutTime);
            aliasToUserMap.put(alias,user);
            return user;
        }
        return null;
    }

    @Override
    public Team registerTeam(String alias, List<User> users) {
        if(!aliasToTeamMap.containsKey(alias)) {
            final Team team = new Team(alias, users);
            aliasToTeamMap.put(alias, team);
            return team;
        }
        return null;
    }

    @Override
    public Optional<User> getUserByAlias(String alias) {
        if(aliasToUserMap.containsKey(alias)) {
            return Optional.of(aliasToUserMap.get(alias));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Team> getTeamByAlias(String alias) {
        if(aliasToTeamMap.containsKey(alias)) {
            return Optional.of(aliasToTeamMap.get(alias));
        }
        return Optional.empty();
    }

    @Override
    public List<User> resolveUniqueUsers(List<Team> teams) {
        final Set<User> uniqueUsers = new HashSet<>();
        teams.forEach(team -> {
            uniqueUsers.addAll(team.getUsers());
        });
        return uniqueUsers.stream().toList();
    }
}
