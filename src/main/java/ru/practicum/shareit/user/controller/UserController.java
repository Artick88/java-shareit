package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") long userId) {
        log.info("Get user by id {}", userId);
        return userService.get(userId);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody UserCreateDto user) {
        log.info("Create user {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable("id") long userId,
                          @Valid @RequestBody UserUpdateDto user) {
        log.info("Update user {}, data {}", user, user);
        return userService.update(userId, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") long userId) {
        log.info("Delete user by id {}", userId);
        userService.delete(userId);
    }
}
