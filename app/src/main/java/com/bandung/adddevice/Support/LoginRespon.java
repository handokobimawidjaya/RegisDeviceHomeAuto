package com.bandung.adddevice.Support;

public class LoginRespon {
    private boolean error;
    private String message;
    private User user;

    public LoginRespon(boolean error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
