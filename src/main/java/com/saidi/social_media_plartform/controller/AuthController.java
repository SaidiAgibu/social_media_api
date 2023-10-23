package com.saidi.social_media_plartform.controller;

import com.saidi.social_media_plartform.dto.LoginDto;
import com.saidi.social_media_plartform.dto.RegisterDto;
import com.saidi.social_media_plartform.exceptions.AlreadyExistsException;
import com.saidi.social_media_plartform.response.LoginResponse;
import com.saidi.social_media_plartform.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Data
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) throws AlreadyExistsException {
        return userService.registerUser(registerDto);
    }

    @GetMapping
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        return userService.verifyAccount(token);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginDto loginDto) {
        return userService.loginUser(loginDto);
    }

}
