package com.gomz.festivallineuptracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {

    private CurrentUser() {
    }




    public static Integer idOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUserDetails details) {
            return details.getId();
        }
        return null;
    }




    public static int requireId() {
        Integer userId = idOrNull();
        if (userId == null) {
            throw new IllegalStateException("Authenticated details are required");
        }
        return userId;
    }
}
