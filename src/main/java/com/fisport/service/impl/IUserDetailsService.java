package com.fisport.service.impl;

import com.fisport.repository.UserRepository;
import com.fisport.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
}
