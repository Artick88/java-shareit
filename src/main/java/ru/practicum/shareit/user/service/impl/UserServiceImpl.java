package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exeption.DuplicationException;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto get(Long id) {
        return userMapper.toUserDto(validationFindUserById(id));
    }

    @Override
    @Transactional
    public UserDto create(UserCreateDto data) {
        User user = userMapper.toUser(data);
        //validationFindDuplicateEmail(user);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserUpdateDto data) {
        User user = userMapper.toUser(id, data);
        User userSaved = validationFindUserById(id);

        if (user.getName() != null) {
            userSaved.setName(user.getName());
        }

        if (user.getEmail() != null) {
            //validationFindDuplicateEmail(user);
            userSaved.setEmail(user.getEmail());
        }

        return userMapper.toUserDto(userSaved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        validationFindUserById(id);
        userRepository.deleteById(id);
    }

    @Deprecated
    private void validationFindDuplicateEmail(User user) {
        User userValidation = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userValidation != null && !Objects.equals(userValidation.getId(), user.getId())) {
            throw new DuplicationException(String.format("Клиент с таким email %s уже существует", user.getEmail()));
        }
    }

    @Override
    public User validationFindUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(String.format("Клиент с ид %d не найден", userId)));
    }
}
