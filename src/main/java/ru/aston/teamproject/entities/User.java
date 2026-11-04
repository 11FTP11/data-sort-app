package ru.aston.teamproject.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = User.UserBuilder.class)
public class User {
    private final String nickname;
    private final String password;
    private final String email;
    private final int id;

    User(UserBuilder builder) {
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.email = builder.email;
        this.id = builder.id;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getNickname() { return nickname; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public int getId() { return id; }
    
    @JsonPOJOBuilder(withPrefix = "set")
    public static class UserBuilder {
        String nickname;
        String password;
        String email;
        private static int idCounter = 0;
        int id;

        public UserBuilder setNickname(String nickname) {
            if (nickname == null || nickname.isBlank()) {
                throw new IllegalArgumentException("Nickname must not be null or empty");
            }
            this.nickname = nickname;
            return this;
        }

        public UserBuilder setPassword(String password) {
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password must not be null or empty");
            }
            this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException("Email must not be null or empty");
            }
            if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Invalid email format: " + email);
            }
            this.email = email;
            return this;
        }

        public User build() {
            this.id = ++idCounter;
            return new User(this);
        }
    }
}
