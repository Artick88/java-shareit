package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
}
