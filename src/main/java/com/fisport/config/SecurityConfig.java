package com.fisport.config;

import com.fisport.security.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOidcUserService customOidcUserService;
    private final PreFilter requestFilter;

    public static final List<String> WHITE_LIST = List.of( "/common/**", "/web/**", "/web/css/**", "/web/img/**", "/favicon.ico",
            "/san/**", "/login/**", "/2fa/**", "/", "/web/js/**", "/forgot-password", "/register**", "/error/**", "/confirm/**", "2fa-register", "/thach-dau/**"); //test


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( "/api/auth/**").permitAll()
                        .requestMatchers( new RegexRequestMatcher("/san.+/dat-san", null)).authenticated()
                        .requestMatchers(WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/owner/**").hasAnyRole("OWNER", "ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "OWNER")
                        .anyRequest().authenticated()
                )

                .formLogin(AbstractHttpConfigurer::disable)

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)

                .userDetailsService(customUserDetailsService)

//                .oauth2Login(oauth2 -> oauth2
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/")
//                        .userInfoEndpoint(info -> info
//                                .oidcUserService(customOidcUserService))
//                        .failureUrl("/login?error=true")
//                        .successHandler(oAuth2SuccessHandler)
//                )

//                .exceptionHandling(ex -> ex
//                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                        .accessDeniedPage("/access-denied")
//                        .accessDeniedHandler(customAccessDeniedHandler))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer ignoreResources() {
        return webSecurity ->
                webSecurity.ignoring()
                        .requestMatchers("/actuator/**", "/v3/**", "/webjars/**", "/swagger-ui*/*swagger-initializer.js", "/swagger-ui*/**");
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**");
    }

}
