package com.Fisport.service.impl;

import com.Fisport.repository.UserRepository;
import com.Fisport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IUserService implements UserService {
    private final UserRepository userRepository;
}
