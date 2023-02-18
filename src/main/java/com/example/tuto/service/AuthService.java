package com.example.tuto.service;

import com.example.tuto.dto.LoginDto;
import com.example.tuto.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
