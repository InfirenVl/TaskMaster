package com.infiren.taskmaster.controller;

import com.infiren.taskmaster.object.dto.RegRequestDto;
import com.infiren.taskmaster.object.dto.UserDto;
import com.infiren.taskmaster.service.AuthService;
import com.infiren.taskmaster.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        log.info("Constructor - AuthController()");
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> createUser(@RequestBody RegRequestDto newUser){
        log.info("@POST - createUser request");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.createUser(newUser));
    }
}
