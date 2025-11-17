package com.fisport.security;

import com.fisport.model.User;
import com.fisport.common.EUserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serializable;
import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails, OidcUser, Serializable {

    private final User user;

    private Map<String, Object> attributes;

    private final OidcIdToken idToken;

    private final OidcUserInfo userInfo;

    public CustomUserDetails(User user) {
        this.user = user;
        this.attributes = null;
        this.idToken = null;
        this.userInfo = null;
    }

    public CustomUserDetails(User user, OidcUser oidcUser) {
        this.user = user;
        this.attributes = new HashMap<>(oidcUser.getAttributes());
        this.idToken = oidcUser.getIdToken();
        this.userInfo = oidcUser.getUserInfo();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return EUserStatus.ACTIVE.equals(user.getStatus());
    }


    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }
}