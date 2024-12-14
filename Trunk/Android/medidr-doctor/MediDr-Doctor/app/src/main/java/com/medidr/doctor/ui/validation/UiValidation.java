package com.medidr.doctor.ui.validation;

import android.util.Log;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Sharvee on 3/8/2016.
 */
public class UiValidation {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$";//"^(\\+91[\\-\\s]?)?[89]\\d{9}$";
    private static final String PINCODE_REGEX = "/^[0-9]{1,6}$/";

    // Error Messages
    private static final String REQUIRED_MSG = "is mandatory field";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "###-#######";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check email validation
    public static boolean isValidEmailAddress(EditText editText) {
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
        // pattern doesn't match so returning false
        if (!Pattern.matches(EMAIL_REGEX, text)) {
            editText.setError("Please enter valid Email");
            return false;
        };

        return true;
    }
    public static boolean isValidMobileNumber(EditText editText) {
        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
        // pattern doesn't match so returning false
        if (!Pattern.matches(PHONE_REGEX, text)) {
            editText.setError("Please enter valid Mobile");
            return false;
        };

        return true;
    }
    public static boolean isValidPincode(EditText editText) {
/*        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);
        // pattern doesn't match so returning false
        if (!Pattern.matches(PINCODE_REGEX, text)) {
            editText.setError("Please enter valid PinCode");
            return false;
        };*/

        return true;
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        Log.d(text, "edit text val is"+text);
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            String strerror=editText.getHint().toString();
            String strsetmsg=strerror.replace("*", "");
            editText.setError(strsetmsg+ " "+REQUIRED_MSG);
            return false;
        }

        return true;
    }
}
