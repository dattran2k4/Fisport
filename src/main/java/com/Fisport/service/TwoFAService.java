package com.Fisport.service;

import com.Fisport.model.User;
import com.Fisport.repository.UserRepository;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TwoFAService {

    private final GoogleAuthenticator gAuth = new GoogleAuthenticator();
    private final UserRepository userRepository;


    /**
     * Enable 2FA For User
     * @param user
     * @param code
     */
    public void enable2Fa(User user, int code){
        if (!verifyCode(user.getTwoFASecret(), code)) {
            throw new IllegalArgumentException("Mã 2FA không hợp lệ");
        }

        if (user.isTwoFAEnable()) {
            throw new IllegalArgumentException("2FA đã được bật trước đó");
        }

        user.setTwoFAEnable(true);
        userRepository.save(user);
    }

    /**
     * Disable 2FA For User
     * @param user
     * @param code
     */
    public void disable2Fa(User user, int code) {
        if (!user.isTwoFAEnable()) {
            throw new IllegalStateException("2FA chưa được bật");
        }

        if (!verifyCode(user.getTwoFASecret(), code)) {
            throw new IllegalArgumentException("Mã 2FA không hợp lệ");
        }

        user.setTwoFAEnable(false);
        userRepository.save(user);
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
}
