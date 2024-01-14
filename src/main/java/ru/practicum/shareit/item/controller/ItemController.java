package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(value = "from", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Get all item by user id {}", userId);
        return itemService.getAll(userId, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable("id") Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item by id {}", itemId);
        return itemService.get(itemId, userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @RequestBody ItemCreateDto itemCreateDto) {
        log.info("Create item {}, userid {}", itemCreateDto, userId);
        return itemService.create(userId, itemCreateDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable("id") Long itemId,
                          @Valid @RequestBody ItemUpdateDto itemUpdateDto) {
        log.info("Update item {}, userId {}", itemUpdateDto, userId);
        return itemService.update(userId, itemId, itemUpdateDto);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam("text") String text,
                                @RequestParam(value = "from", defaultValue = "0") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Search items - {}", text);
        return itemService.search(text, PageRequest.of(page, size));
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @Valid @RequestBody CommentCreateDto commentCreateDto) {
        log.info("User {} for item {} create comment {}", userId, itemId, commentCreateDto);
        return itemService.createComment(userId, itemId, commentCreateDto);
    }
}
