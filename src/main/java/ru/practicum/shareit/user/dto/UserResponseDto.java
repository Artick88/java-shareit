package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String name;
}
