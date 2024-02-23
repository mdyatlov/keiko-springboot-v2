package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.SignUpForm;
import com.theodo.albeniz.model.UserEntity;
import com.theodo.albeniz.services.UserSignUpService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserSignUpService userSignUpService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserEntity> signUp(@RequestBody @Valid SignUpForm user) {
        return ResponseEntity.ok(userSignUpService.signUp(user));
    }
}
