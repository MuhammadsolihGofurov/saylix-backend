package api.saylix.uz.utils;

import java.util.regex.Pattern;

public class UsernameCheckingUtil {

    public static boolean isValidPhone(String username){
        String phoneRegex = "^998\\d{9}$";
        return Pattern.matches(phoneRegex, username);
    }

    public static boolean isEmailValid(String username){
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(emailRegex, username);
    }

}
