package com.shikhar03stark.model;

import java.util.Random;

public abstract class Participant {
    protected final int Id;
    protected String alias;
    protected String domain;

    public Participant(String alias, String domain) {
        Id = new Random().nextInt();
        this.alias = alias;
        this.domain = domain;
    }

    public String getEmail() {
        return String.format("%s@%s", alias, domain);
    }

    public int getId() {
        return Id;
    }

    public String getAlias() {
        return alias;
    }

    public String getDomain() {
        return domain;
    }
}
