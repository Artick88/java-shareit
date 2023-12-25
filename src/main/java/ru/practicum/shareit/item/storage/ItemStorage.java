package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    List<Item> getAll(Long userId);

    Item get(Long itemId);

    Item create(Item item);

    Item update(Item item);

    List<Item> getAllItems();
}
