package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(Long id);

    User create(UserCreateDto user);

    User update(Long id, UserUpdateDto user);

    void delete(Long id);

    User validationFindUserById(Long userId);
}
