package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto get(Long itemId);

    List<ItemDto> getAll(Long userId);

    ItemDto create(Long userId, ItemCreateDto itemCreateDto);

    ItemDto update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto);

    List<ItemDto> search(String text);
}
