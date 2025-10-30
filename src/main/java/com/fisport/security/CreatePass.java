package com.fisport.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CreatePass {
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }
}
