package com.ecommece.dto;

public class UserUpdateRequest {
    private String name;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
