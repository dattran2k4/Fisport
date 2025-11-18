package com.fisport.config;

import com.fisport.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomSuccessHandler customSuccessHandler;
    private final TwoFactorAuthFilter twoFactorAuthFilter;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    // White list tổng hợp
    public static final List<String> WHITE_LIST = List.of(
            "/common/**", "/web/**", "/web/css/**", "/web/img/**", "/web/js/**",
            "/favicon.ico", "/san/**", "/", "/login/**", "/2fa/**",
            "/forgot-password", "/register**", "/error/**", "/confirm/**", "2fa-register",
            "/swagger-ui/**", "/v3/api-docs/**", "/api/payment/webhook/**"
    );

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // có thể tăng strength nếu cần
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(customUserDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // Cho phép các request công khai
                        .requestMatchers(WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/fields/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/field-types/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/sub-fields/**").permitAll()
                        .requestMatchers("/api/**").permitAll()

                        // Yêu cầu xác thực
                        .requestMatchers(new RegexRequestMatcher("/san.+/dat-san", null)).authenticated()

                        // Phân quyền
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/owner/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "OWNER")

                        .anyRequest().authenticated()
                )
                // Bật form login custom
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/perform_login")
                        .successHandler(customSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .addFilterBefore(twoFactorAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .accessDeniedPage("/access-denied")
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        // Register custom provider
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}
