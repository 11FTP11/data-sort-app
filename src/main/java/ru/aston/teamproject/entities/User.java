package ru.aston.teamproject.entities;

public class User implements Comparable<User> {
    private final String nickname;
    private final String password;
    private final String email;
    private final int scores;

    User(UserBuilder builder) {
        this.nickname = builder.nickname;
        this.password = builder.password;
        this.email = builder.email;
        this.scores = builder.scores;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getScores() {
        return scores;
    }

    @Override
    public int compareTo(User other) {
        int nicknameComparison = this.nickname.compareTo(other.nickname);
        if (nicknameComparison != 0) {
            return nicknameComparison;
        }

        int emailComparison = this.email.compareTo(other.email);
        if (emailComparison != 0) {
            return emailComparison;
        }

        return Integer.compare(this.scores, other.scores);
    }

    public static class UserBuilder {
        String nickname;
        String password;
        String email;
        int scores;

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

        public UserBuilder setScores(Integer scores) {
            if (scores == null) {
                throw new IllegalArgumentException("Scores must not be null or empty");
            }
            this.scores = Math.clamp(scores, 0, 100);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

}
