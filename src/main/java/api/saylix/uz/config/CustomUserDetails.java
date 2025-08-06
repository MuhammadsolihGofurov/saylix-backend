package api.saylix.uz.config;

import api.saylix.uz.entity.UsersEntity;
import api.saylix.uz.enums.UsersRoles;
import api.saylix.uz.enums.UsersStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final String id;
    private final String username;
    private final String password;
    private final UsersStatus status;
    private final UsersRoles role;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UsersEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.role = user.getUserRole();

        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    public String getId() {
        return id;
    }

    public UsersRoles getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // sozlanishi mumkin
    }

    @Override
    public boolean isAccountNonLocked() {
        return status == UsersStatus.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // sozlanishi mumkin
    }

    @Override
    public boolean isEnabled() {
        return status == UsersStatus.ACTIVE;
    }
}
