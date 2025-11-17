package com.fisport.security;

import com.fisport.common.ERole;
import com.fisport.common.EUserStatus;
import com.fisport.exception.ResourceNotFoundException;
import com.fisport.model.Role;
import com.fisport.model.User;
import com.fisport.model.Wallet;
import com.fisport.repository.RoleRepository;
import com.fisport.repository.UserRepository;
import com.fisport.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CustomOidcUserService")
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WalletRepository walletRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);
        Map<String, Object> attributes = oidcUser.getAttributes();

        User userEntity = processOAuthUser(attributes);

        return new CustomUserDetails(userEntity, oidcUser);
    }

    private User processOAuthUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            return userOpt.get();
        }

        User newUser = new User();

        newUser.setUsername(fullName);
        newUser.setEmail(email);
        newUser.setStatus(EUserStatus.ACTIVE);
        newUser.setTwoFAEnable(false);
        newUser.setPassword(null);

        Role role = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found"));
        newUser.setRole(role);

        userRepository.save(newUser);


        //add wallet
        Wallet wallet = new Wallet();
        wallet.setUser(newUser);

        walletRepository.save(wallet);

        log.info("Created walletId {} for userId {}", wallet.getId(), newUser.getId());

        newUser.setWallet(wallet);

        return userRepository.save(newUser);
    }
}
