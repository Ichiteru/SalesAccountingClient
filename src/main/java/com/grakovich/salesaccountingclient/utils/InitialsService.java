package com.grakovich.salesaccountingclient.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InitialsService {
    private final static String INITIALS_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    private final static String EMAIL_REGEX = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";

    private final Pattern initialsPattern = Pattern.compile(INITIALS_REGEX);
    private final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    public boolean isValidName(String initials) {
        if (initials == null) {
            return false;
        }
        Matcher loginMatcher = initialsPattern.matcher(initials);

        return loginMatcher.matches();
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher loginMatcher = emailPattern.matcher(email);

        return loginMatcher.matches();
    }

}
