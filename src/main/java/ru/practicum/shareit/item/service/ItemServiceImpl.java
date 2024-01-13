package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public ItemDto get(Long itemId) {
        return itemMapper.toItemDto(validationFindItemById(itemId));
    }

    @Override
    public List<ItemDto> getAll(Long userId) {
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto create(Long userId, ItemCreateDto itemCreateDto) {
        User user = userService.validationFindUserById(userId);
        Item item = itemMapper.toItem(itemCreateDto);
        item.setOwner(user);

        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemUpdateDto) {
        userService.validationFindUserById(userId);
        Item itemSaved = validationOwnerUserById(userId, itemId);
        Item item = itemMapper.toItem(itemUpdateDto);

        if (item.getName() != null) {
            itemSaved.setName(item.getName());
        }

        if (item.getDescription() != null) {
            itemSaved.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            itemSaved.setAvailable(item.getAvailable());
        }

        return itemMapper.toItemDto(itemSaved);
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return itemRepository.search(text).stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item validationFindItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException(String.format("Не найден объект с ид %d", itemId)));
    }

    private Item validationOwnerUserById(Long userId, Long itemId) {
        Item item = validationFindItemById(itemId);
        if (!Objects.equals(item.getOwner().getId(), userId)) {
            throw new NotFoundException("Пользователь не является владельцем");
        }
        return item;
    }
}
