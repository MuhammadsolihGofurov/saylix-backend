package api.saylix.uz.utils;


import api.saylix.uz.config.CustomUserDetails;
import api.saylix.uz.enums.UsersRoles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SpringSecurityUtil {

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

        return user;
    }

    public static String getCurrentUserId() {
        CustomUserDetails user = getCurrentUser();
        return user.getId();
    }


    public static Boolean hasRole(UsersRoles requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getRole().equals(requiredRole);
        }

        return false;
    }

    public static UsersRoles getCurrentUserRole() {
        return getCurrentUser().getRole();
    }


}
