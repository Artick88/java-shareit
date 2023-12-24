package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    public User toUser(UserCreateDto userCreateDto) {
        return User.builder()
                .email(userCreateDto.getEmail())
                .name(userCreateDto.getName())
                .build();
    }

    public User toUser(UserUpdateDto userUpdateDto) {
        return User.builder()
                .email(userUpdateDto.getEmail())
                .name(userUpdateDto.getName())
                .build();
    }

    public UserResponseDto toUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
