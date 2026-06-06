package com.infiren.taskmaster.service;

import com.infiren.taskmaster.object.dto.RegRequestDto;
import com.infiren.taskmaster.object.dto.UserDto;
import com.infiren.taskmaster.object.entity.UserEntity;
import com.infiren.taskmaster.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDto createUser(RegRequestDto newUser) {
        UserEntity user = new UserEntity();

//        user.setCreatedAt(LocalDateTime.now().plusSeconds(1));
//        user.setEmail(newUser.getEmail());
//        user.setUsername(newUser.getUsername());
//        user.setPasswordHash(passwordEncoder.encode(newUser.getPassword()));
//
//        //TO-DO UserDtoMapper from RequestDto
//
//        return UserDto;
        return null;
    }
}
