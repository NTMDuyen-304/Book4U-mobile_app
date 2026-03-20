package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String name;
    private String email;
    private String role;

    public User() {
    }

    public User(String id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}