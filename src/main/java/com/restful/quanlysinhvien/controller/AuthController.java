package com.restful.quanlysinhvien.controller;

import org.springframework.web.bind.annotation.RestController;

import com.restful.quanlysinhvien.domain.dto.LoginDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> postMethodName(@Valid @RequestBody LoginDTO loginDTO) {
        // Xác thực thông tin đăng nhập
        // Authentication authentication = authenticationManager.authenticate(
        // new UsernamePasswordAuthenticationToken(
        // loginDTO.getUsername(),
        // loginDTO.getPassword()));

        // // Lưu thông tin xác thực vào SecurityContext
        // SecurityContextHolder.getContext().setAuthentication(authentication);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(),
                loginDTO.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        return ResponseEntity.ok(loginDTO);
    }

}
