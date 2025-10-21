package com.Fisport.service;

import com.Fisport.dto.response.TwoFAResponse;
import com.Fisport.model.User;
import com.Fisport.repository.UserRepository;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.util.QRCodeUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TwoFAService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();
    private final UserRepository userRepository;
    private final QRCodeUtil  qrCodeUtil;
    private final SecurityContextService securityContextService;

    /** Generate2FA For existed user
     * @param username
     * @return
     */
    public TwoFAResponse generate2FASecret(String username) {

        User user = userRepository.findByUsername(username).orElse(null);

        //User has
        if (user.getTwoFASecret() != null) {
            String url = getOtpAuthURL(username, user.getTwoFASecret());
            String qrCode = qrCodeUtil.generateQRCodeBase64(url, 200, 200);
            return TwoFAResponse.builder()
                    .url(url)
                    .qrCode(qrCode)
                    .build();
        }

        String secret = generateSecret();
        String url = getOtpAuthURL(username, secret);
        String qrCode = qrCodeUtil.generateQRCodeBase64(url, 200, 200);

        //add secret
        user.setTwoFASecret(secret);
        userRepository.save(user);

        return TwoFAResponse.builder()
                .url(url)
                .qrCode(qrCode)
                .build();
    }


    /**
     * Enable 2FA For User
     * @param username
     * @param code
     */
    public void enable2Fa(String username, String code) {

        User user = userRepository.findByUsername(username).orElse(null);

        if (!verifyCode(user.getTwoFASecret(), Integer.parseInt(code))) {
            throw new IllegalArgumentException("Mã 2FA không hợp lệ");
        }

        if (user.isTwoFAEnable()) {
            throw new IllegalArgumentException("2FA đã được bật trước đó");
        }

        user.setTwoFAEnable(true);
        userRepository.save(user);

        securityContextService.updateAuthentication(user);
    }

    /**
     * Disable 2FA For User
     * @param username
     * @param code
     */
    public void disable2Fa(String username, String code) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (!user.isTwoFAEnable()) {
            throw new IllegalStateException("2FA chưa được bật");
        }

        if (!verifyCode(user.getTwoFASecret(), Integer.parseInt(code))) {
            throw new IllegalArgumentException("Mã 2FA không hợp lệ");
        }

        user.setTwoFAEnable(false);
        userRepository.save(user);

        securityContextService.updateAuthentication(user);
    }

    public String generateSecret() {
        GoogleAuthenticatorKey key = gAuth.createCredentials();
        return key.getKey();
    }

    // Verify OTP 6 digits from app
    public boolean verifyCode(String secret, int code) {
        return gAuth.authorize(secret, code);
    }

    public String getOtpAuthURL(String username, String secret) {
        //To-do URLEncoder

        return String.format("otpauth://totp/Fisport:%s?secret=%s&issuer=Fisport", username, secret);
    }

    public void updateUserAuthentication(User user) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails updatedPrincipal = new CustomUserDetails(user); // principal mới
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                currentAuth.getCredentials(),
                updatedPrincipal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
