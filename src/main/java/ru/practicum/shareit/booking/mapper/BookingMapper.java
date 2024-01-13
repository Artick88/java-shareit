package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public Booking toBooking(Long userId, BookingCreateDto bookingCreateDto) {
        return Booking.builder()
                .startDate(bookingCreateDto.getStart())
                .endDate(bookingCreateDto.getEnd())
                .status(BookingStatus.WAITING)
                .item(Item.builder()
                        .id(bookingCreateDto.getItemId())
                        .build())
                .booker(User.builder()
                        .id(userId)
                        .build())
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .booker(UserDto.builder()
                        .id(booking.getBooker().getId())
                        .name(booking.getBooker().getName())
                        .email(booking.getBooker().getEmail())
                        .build())
                .status(booking.getStatus())
                .item(ItemDto.builder()
                        .id(booking.getItem().getId())
                        .name(booking.getItem().getName())
                        .description(booking.getItem().getDescription())
                        .available(booking.getItem().getAvailable())
                        .build())
                .build();
    }

    public List<BookingDto> toBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDto)
                .collect(Collectors.toList());
    }

    public BookingItemDto toBookingItemDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .build();
    }

    public List<BookingItemDto> toBookingItemDto(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingItemDto)
                .collect(Collectors.toList());
    }
}
