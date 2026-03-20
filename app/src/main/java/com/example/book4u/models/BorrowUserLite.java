package com.example.book4u.models;

import com.google.gson.annotations.SerializedName;

public class BorrowUserLite {
    @SerializedName(value = "id", alternate = {"_id"})
    private String id;

    private String name;
    private String email;
    private String role;

    public String getId() {
        return id;
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