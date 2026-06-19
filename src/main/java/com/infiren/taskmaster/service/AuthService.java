package com.infiren.taskmaster.service;

import com.infiren.taskmaster.object.dto.RegRequestDto;
import com.infiren.taskmaster.object.dto.UserDto;
import com.infiren.taskmaster.object.entity.UserEntity;
import com.infiren.taskmaster.object.mapper.UserMapper;
import com.infiren.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional

public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDto createUser(RegRequestDto newUser) {
        UserEntity user = new UserEntity();

        user.setCreatedAt(LocalDateTime.now().plusSeconds(1));
        user.setEmail(newUser.getEmail());
        user.setUsername(newUser.getUsername());
        user.setPasswordHash(passwordEncoder.encode(newUser.getPassword()));

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }
}
