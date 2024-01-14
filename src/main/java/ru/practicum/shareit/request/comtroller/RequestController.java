package ru.practicum.shareit.request.comtroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @Valid @RequestBody RequestCreateDto requestCreateDto) {
        log.info("User {} create item request {}", userId, requestCreateDto);
        return requestService.create(userId, requestCreateDto);
    }

    @GetMapping
    public List<RequestDto> getAllUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("User {} get all item request", userId);
        return requestService.getAllUserById(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @RequestParam(value = "from", defaultValue = "0") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("User {} get all item request", userId);
        return requestService.getAll(userId, PageRequest.of(page, size));
    }

    @GetMapping("/{requestId}")
    public RequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long requestId) {
        log.info("User {} get item request {}", userId, requestId);
        return requestService.getById(userId, requestId);
    }

}
