package com.Fisport.service.impl;

import com.Fisport.repository.UserRepository;
import com.Fisport.service.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
}
