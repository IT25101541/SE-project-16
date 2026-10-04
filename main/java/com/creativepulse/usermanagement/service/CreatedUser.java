package com.creativepulse.usermanagement.service;

import com.creativepulse.usermanagement.model.User;

/** Result of an admin creating an account: the user plus the one-time temporary password. */
public class CreatedUser {

    private final User user;
    private final String temporaryPassword;

    public CreatedUser(User user, String temporaryPassword) {
        this.user = user;
        this.temporaryPassword = temporaryPassword;
    }

    public User getUser() { return user; }
    public String getTemporaryPassword() { return temporaryPassword; }
}
