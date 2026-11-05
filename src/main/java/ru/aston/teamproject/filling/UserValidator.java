package ru.aston.teamproject.filling;

public class UserValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public boolean isValid(String nickname, String password, String email) {
        return !isBlank(nickname) && isValidPassword(password) && isValidEmail(email);
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    private boolean isValidPassword(String pass) {
        return pass != null && pass.length() >= 5;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}