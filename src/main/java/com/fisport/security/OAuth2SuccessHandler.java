package com.fisport.security;

import com.fisport.common.ERole;
import com.fisport.common.EUserStatus;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Role;
import com.fisport.model.User;
import com.fisport.repository.RoleRepository;
import com.fisport.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "OAuth2SuccessHandler")
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken  token = (OAuth2AuthenticationToken) authentication;

        Map<String,Object> attributes = token.getPrincipal().getAttributes();

        String email = (String) attributes.get("email");

        String fullName = (String) attributes.get("name");


        Optional<User> userOpt = userRepository.findByEmail(email);

        User user = null;

        if (userOpt.isEmpty()) {
            User  newUser = new User();
            newUser.setUsername(fullName);
            newUser.setEmail(email);
            newUser.setStatus(EUserStatus.ACTIVE);
            newUser.setTwoFAEnable(false);
            newUser.setPassword(null);

            Role role = roleRepository.findByName(ERole.USER).orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found"));
            newUser.setRole(role);

            userRepository.save(newUser);
        } else {
            user = userOpt.get();
        }

        String roleName = "ROLE_" + user.getRole().getName().toString();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                token.getPrincipal(),
                authorities,
                token.getAuthorizedClientRegistrationId()
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        setDefaultTargetUrl("/");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
