package sample;

import java.util.regex.Pattern;

public class Validator {

    public boolean isValidPassword(String password) {
        String patternString = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{16,}";
        return Pattern.matches(patternString, password);
    }
}
