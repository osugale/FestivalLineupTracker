package com.gomz.festivallineuptracker.dto;

import com.gomz.festivallineuptracker.model.Role;

public class LoginResponseDTO {

    private String token;
    private int id;
    private String username;
    private Role role;

    public LoginResponseDTO(){

    }

    public String getToken() {return token;}
    public int getId() {return id;}
    public String getUsername() {return username;}
    public Role getRole() {return role;}

    public void setToken(String token) {this.token = token;}
    public void setId(int id) {this.id = id;}
    public void setUsername(String username) {this.username = username;}
    public void setRole(Role role) {this.role = role;}
}
