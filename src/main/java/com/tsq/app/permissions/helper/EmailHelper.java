package com.tsq.app.permissions.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailHelper {

  private static final String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[a-z]{2,4}$";

  public static boolean isValidEmail(String email) {
    boolean isValidEmail = false;

    if (email != null && email.length() > 0) {

      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(email);

      if (matcher.matches()) {
        isValidEmail = true;
      }
    }

    return isValidEmail;
  }

}
