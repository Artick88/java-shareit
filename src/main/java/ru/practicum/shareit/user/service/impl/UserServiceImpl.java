package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exeption.DuplicationException;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto get(Long id) {
        return userMapper.toUserDto(userStorage.get(id));
    }

    @Override
    public UserDto create(UserCreateDto data) {
        User user = userMapper.toUser(data);
        validation(user);
        return userMapper.toUserDto(userStorage.create(user));
    }

    @Override
    public UserDto update(Long id, UserUpdateDto data) {
        User user = userMapper.toUser(data);
        user.setId(id);
        validation(user);
        return userMapper.toUserDto(userStorage.update(user));
    }

    @Override
    public void delete(Long id) {
        validationFindUserById(id);
        userStorage.delete(id);
    }

    private void validation(User user) {
        if (user.getId() != null) {
            validationFindUserById(user.getId());
        }

        if (user.getEmail() != null) {
            User userValidation = userStorage.getByEmail(user.getEmail());
            if (userValidation != null && !Objects.equals(userValidation.getId(), user.getId())) {
                throw new DuplicationException(String.format("Клиент с таким email %s уже существует", user.getEmail()));
            }
        }
    }

    @Override
    public User validationFindUserById(Long userId) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Клиент с ид %d не найден", userId));
        }
        return user;
    }
}
