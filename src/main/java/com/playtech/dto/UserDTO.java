package com.playtech.dto;

public class UserDTO {
    private String userName;

    public UserDTO(String userName) {
        this.userName = userName;
    }

    public UserDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
