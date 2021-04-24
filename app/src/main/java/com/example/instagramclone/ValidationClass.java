package com.example.instagramclone;

import android.util.Patterns;
import android.widget.EditText;
import java.util.regex.Pattern;

public class ValidationClass {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    public static boolean validateEmail(EditText editText) {
        String emailInput = editText.getText().toString().trim();
        if (emailInput.isEmpty()) {
            editText.setError("Field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            editText.setError("Please enter a valid email address!");
            return false;
        } else {
            editText.setError((CharSequence) null);
            return true;
        }
    }

    public static boolean validatePhone(EditText editText) {
        String phoneInput = editText.getText().toString().trim();
        if (phoneInput.isEmpty()) {
            editText.setError("Field can't be empty");
            return false;
        } else if (!Patterns.PHONE.matcher(phoneInput).matches()) {
            editText.setError("Please enter a valid phone no!");
            return false;
        } else if (phoneInput.length() < 10) {
            editText.setError("Please enter a valid phone no!");
            return false;
        } else {
            editText.setError((CharSequence) null);
            return true;
        }
    }

    public static boolean validatePassword(EditText editText1, EditText editText2) {
        String passwordInput = editText1.getText().toString().trim();
        String rePasswordInput = editText2.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            editText1.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            editText1.setError("Password is too weak");
            return false;
        } else if (!rePasswordInput.isEmpty()) {
            if (passwordInput.equals(rePasswordInput)) {
                return true;
            }
            editText2.setError("password must be same...!");
            return false;
        } else if (passwordInput.isEmpty()) {
            editText1.setError((CharSequence) null);
            return true;
        } else if (!rePasswordInput.isEmpty()) {
            return true;
        } else {
            editText2.setError("Field can't be empty");
            return false;
        }
    }
}
