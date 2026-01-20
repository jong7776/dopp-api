package com.dopp.doppapi.service.auth;

import com.dopp.doppapi.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;

    public boolean isPasswordValid(UserDto user, String password) {
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return true;
        }
        return false;
    }

    public boolean isUserActiveValid(UserDto user) {
        if (user.getIsActive() == null || !user.getIsActive()) {
            return true;
        }
        return false;
    }

    public String getPasswordEncode(String password) {
        return passwordEncoder.encode(password);
    }
}
