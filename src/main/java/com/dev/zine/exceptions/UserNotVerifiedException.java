package com.dev.zine.exceptions;

public class UserNotVerifiedException extends Exception {

    private boolean newEmailSent;

    public UserNotVerifiedException(boolean newEmailSent) {
        super("Email not verified.");
        this.newEmailSent = newEmailSent;
    }

    public boolean isNewEmailSent() {
        return newEmailSent;
    }

}
