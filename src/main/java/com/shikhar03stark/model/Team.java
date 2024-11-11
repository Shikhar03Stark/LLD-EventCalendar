package com.shikhar03stark.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team extends Participant{
    private final List<User> users;

    public Team(String alias, List<User> users) {
        super(alias, "microsoft.com");
        if (Objects.nonNull(users)){
            this.users = users;
        } else {
            this.users = new ArrayList<>();
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        if (users.stream().noneMatch(u -> u.Id == user.Id)) {
            users.add(user);
        }
    }
}
