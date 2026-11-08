package ru.aston.teamproject.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ru.aston.teamproject.strategies.filling.Fillable;

import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

@JsonDeserialize(builder = User.UserBuilder.class)
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
    
    public static UserBuilder builder(boolean strict) {
        return new UserBuilder(strict);
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

    @Override
    public String toString() {
        return String.format("info %s:\n - password: %s;\n - email: %s\n - scores: %s\n",
                this.getNickname(), this.getPassword(), this.getEmail(), this.getScores());
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class UserBuilder implements Fillable<User> {

        private static final String[] NAMES = {"Mark", "Maria", "Ivan", "Olga", "Dmitriy", "Elena", "Denis", "Aleksey", "Arina", "Maxim", "Magomed", "Albert", "Kirill", "Anastasia", "Larisa", "Victoria", "Victor", "Mihail", "Anatoliy", "Oleg", "Karina", "Valentina", "Fatima", "Kerim"};
        private static final String[] DOMAINS = {"@gmail.com", "@yandex.ru", "@ya.ru", "@mail.ru", "@list.ru", "@bk.ru", "@inbox.ru", "@xmail.ru"};
        private static final Random RANDOM = new Random();

        String nickname;
        String password;
        String email;
        boolean strict;
        int scores;

        public UserBuilder(boolean strict) {
            this.strict = strict;
        }
        
        public UserBuilder() {
            this.strict = true;
        }

        public UserBuilder setNickname(String nickname) {
            if (this.strict && (nickname == null || nickname.isBlank())) {
                throw new IllegalArgumentException("Nickname не должен быть пустым");
            }
            this.nickname = nickname;
            return this;
        }

        public UserBuilder setPassword(String password) {
            if (this.strict && (password == null || password.isBlank() || password.length() <= 5)) {
                throw new IllegalArgumentException("Password должен состоять минимум из 5 символов");
            }
            this.password = password;
            return this;
        }

        public UserBuilder setEmail(String email) {
            if (this.strict && (email == null || email.isBlank())) {
                throw new IllegalArgumentException("Email не должен быть пустым");
            }
            if (this.strict && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException("Неверный формат email: " + email);
            }
            this.email = email;
            return this;
        }

        public UserBuilder setScores(Integer scores) {
            if (this.strict && scores == null) {
                throw new IllegalArgumentException("Scores не должен быть пустым");
            }

            if (scores != null) {
                this.scores = Math.clamp(scores, 0, 100);                
            } else {
                this.scores = -1;
            }

            return this;
        }

        @Override
        public User createRandom() {
            String name = NAMES[RANDOM.nextInt(NAMES.length)];
            return this.setNickname(name)
                    .setPassword("pass" + RANDOM.nextInt(10000))
                    .setEmail(name.toLowerCase() + RANDOM.nextInt(100) + DOMAINS[RANDOM.nextInt(DOMAINS.length)])
                    .setScores(RANDOM.nextInt(101))
                    .build();
        }

        @Override
        public User createManual(Scanner in, PrintStream out) {
            out.print("Введите Nickname: ");
            this.setNickname(in.nextLine().trim());
            out.print("Введите Password (мин. 5 символов): ");
            this.setPassword(in.nextLine().trim());
            out.print("Введите Email: ");
            this.setEmail(in.nextLine().trim());
            out.print("Введите Scores (0-100): ");
            String scores = in.nextLine().trim();
            if (scores.isBlank()) {
                this.setScores(null);
            } else {
                this.setScores(Integer.parseInt(scores));
            }
            return this.build();
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return scores == user.scores &&
                java.util.Objects.equals(nickname, user.nickname) &&
                java.util.Objects.equals(email, user.email) &&
                java.util.Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(nickname, email, password, scores);
    }

}
