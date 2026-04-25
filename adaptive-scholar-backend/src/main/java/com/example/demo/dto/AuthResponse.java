package com.example.demo.dto;

import com.example.demo.model.User;

public class AuthResponse {
    private String token;
    private UserData user;

    public AuthResponse(String token, User userEntity) {
        this.token = token;
        this.user = new UserData(userEntity.getId(), userEntity.getName(), userEntity.getEmail(), userEntity.getRole(), userEntity.getBio());
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public UserData getUser() { return user; }
    public void setUser(UserData user) { this.user = user; }

    public static class UserData {
        private Long id;
        private String name;
        private String email;
        private String role;
        private String bio;

        public UserData(Long id, String name, String email, String role, String bio) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.role = role;
            this.bio = bio;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getBio() { return bio; }
    }
}
