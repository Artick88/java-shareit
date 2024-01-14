package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class RequestMapper {

    public Request toRequest(Long userId, RequestCreateDto requestCreateDto) {
        return Request.builder()
                .description(requestCreateDto.getDescription())
                .author(User.builder().id(userId).build())
                .created(LocalDateTime.now())
                .build();
    }

    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .build();
    }
}
