package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingItemDto;

@Data
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
}
