package com.telematica.travelmate.model;

import java.util.List;

/**
 * Created by Erlend on 12.10.2016.
 */

public class User {
    private String id;
    private String name;
    private String email;
    private List<String> entries;

    public User(String id, String name, String email, List<String> entries) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.entries = entries;
    }
    public User(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }
}
