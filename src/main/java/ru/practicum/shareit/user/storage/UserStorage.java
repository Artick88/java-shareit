package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAll();

    User get(Long id);

    User create(User user);

    User update(User user);

    void delete(Long id);

    User getByEmail(String email);
}
