package com.Fisport.service.impl;

import com.Fisport.dto.request.LoginRequestDTO;
import com.Fisport.dto.request.RegisterRequestDTO;
import com.Fisport.dto.response.LoginResponse;
import com.Fisport.dto.response.LoginResponseDTO;
import com.Fisport.dto.response.RegisterResponseDTO;
import com.Fisport.exception.InvalidDataException;
import com.Fisport.exception.ResourceNotFoundException;
import com.Fisport.model.Role;
import com.Fisport.model.User;
import com.Fisport.repository.RoleRepository;
import com.Fisport.repository.UserRepository;
import com.Fisport.security.CustomUserDetails;
import com.Fisport.service.AuthService;
import com.Fisport.util.ERole;
import com.Fisport.util.EUserStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;



    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Không thấy tài khoản hoặc mật khẩu"));

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new InvalidDataException("Mật khẩu không đúng");
        }
        return LoginResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .status(user.getStatus())
                .roleName(String.valueOf(user.getRole().getName()))
                .build();
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO  registerRequestDTO) {
        if (userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()) {
            throw new InvalidDataException("tài khoản đã tồn tại");
        }
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent()) {
            throw new InvalidDataException("email đã tồn tại");
        }

        if (userRepository.findByPhone(registerRequestDTO.getPhone()).isPresent()) {
            throw new InvalidDataException("số điện thoại đã tồn tại");
        }

        Role role = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role 'USER' not found"));

        User user = User.builder()
                .username(registerRequestDTO.getUsername())
                .email(registerRequestDTO.getEmail())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .phone(registerRequestDTO.getPhone())
                .birthday(registerRequestDTO.getBirthday())
                .gender(registerRequestDTO.getGender())
                .status(EUserStatus.ACTIVE)
                .role(role)
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .birthday(user.getBirthday())
                .gender(user.getGender())
                .status(EUserStatus.ACTIVE)
                .roleName(String.valueOf(user.getRole().getName()))
                .build();
    }

    @Override
    public LoginResponse loginApi(LoginRequestDTO request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println(authentication.getAuthorities());

        // Tạo session để sinh JSESSIONID
        HttpSession session = httpRequest.getSession(true);
        String jsessionId = session.getId();

        new ChangeSessionIdAuthenticationStrategy().onAuthentication(authentication, httpRequest, httpResponse);

        // Lấy thông tin user
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        return LoginResponse.builder()
                .sessionId(jsessionId)
                .userId(userDetails.getUser().getId())
                .username(userDetails.getUsername())
                .birthDate(userDetails.getUser().getBirthday())
                .phoneNumber(userDetails.getUser().getPhone())
                .email(userDetails.getUser().getEmail())
                .gender(String.valueOf(userDetails.getUser().getGender()))
                .role(role)
                .build();
    }
    }



