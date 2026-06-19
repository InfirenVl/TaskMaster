package com.infiren.taskmaster.object.mapper;

import com.infiren.taskmaster.object.dto.UserDto;
import com.infiren.taskmaster.object.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(UserEntity user){
        return new UserDto(user.getUsername(), user.getEmail());
    }
}
