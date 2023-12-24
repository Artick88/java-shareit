package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> userIdMap = new HashMap<>();
    private final Map<String, User> userEmailMap = new HashMap<>();

    private Long id = 0L;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userIdMap.values());
    }

    @Override
    public User get(Long id) {
        return userIdMap.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(++id);
        userIdMap.put(user.getId(), user);
        userEmailMap.put(user.getEmail(), user);
        return userIdMap.get(user.getId());
    }

    @Override
    public User update(User user) {
        User userMap = userIdMap.get(user.getId());
        userIdMap.put(user.getId(), user);

        if (!userEmailMap.containsKey(user.getEmail())) {
            userEmailMap.remove(userMap.getEmail());
        }

        userEmailMap.put(user.getEmail(), user);

        return userIdMap.get(user.getId());
    }

    @Override
    public void delete(Long id) {
        User userMap = userIdMap.get(id);
        userIdMap.remove(id);
        userEmailMap.remove(userMap.getEmail());
    }

    @Override
    public User getByEmail(String email) {
        return userEmailMap.get(email);
    }

}
