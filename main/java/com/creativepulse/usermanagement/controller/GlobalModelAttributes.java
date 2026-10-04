package com.creativepulse.usermanagement.controller;

import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Makes the signed-in user available to every template as ${currentUser}. */
@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUser")
    public User currentUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()) {
            return null;
        }
        try {
            return userService.getByEmail(authentication.getName());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
