package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto get(Long itemId) {
        return itemMapper.toItemDto(itemStorage.get(itemId));
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemStorage.getAll(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto create(Long userId, ItemCreateDto itemCreateDto) {
        User user = userService.validationFindUserById(userId);
        Item item = itemMapper.toItem(itemCreateDto);
        item.setOwner(user);

        return itemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        User user = userService.validationFindUserById(userId);

        validationFindItemById(itemId);
        validationOwnerUserById(userId, itemId);

        Item item = itemMapper.toItem(itemUpdateDto);
        item.setId(itemId);
        item.setOwner(user);

        return itemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemStorage.getAllItems().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase())
                        || item.getName().toLowerCase().contains(text.toLowerCase()))
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void validationFindItemById(Long itemId) {
        if (itemStorage.get(itemId) == null) {
            throw new NotFoundException(String.format("Не найден объект с ид %d", itemId));
        }
    }

    private void validationOwnerUserById(Long userId, Long itemId) {
        if (!Objects.equals(itemStorage.get(itemId).getOwner().getId(), userId)) {
            throw new NotFoundException("Пользователь не является владельцем");
        }
    }
}
