package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingState;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.exception.exeption.NotFoundException;
import ru.practicum.shareit.exception.exeption.NotValidRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.OrderBy;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final ItemService itemService;

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingCreateDto bookingCreateDto) {
        User user = userService.validationFindUserById(userId);
        Item item = itemService.validationFindItemById(bookingCreateDto.getItemId());
        Booking booking = bookingMapper.toBooking(userId, bookingCreateDto);

        validationCreateBooking(user, item, booking);

        bookingRepository.save(booking);
        booking.setItem(item);
        booking.setBooker(user);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = validateFindBookingById(bookingId);
        itemService.validationOwnerUserById(userId, booking.getItem().getId());

        if (approved) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new NotValidRequestException("Повторное потверждение не допустимо");
            }
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        Booking booking = validateFindBookingById(bookingId);
        userService.validationFindUserById(userId);

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Объект не найден, отсутствует доступ");
        }

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @OrderBy("start DESC")
    public List<BookingDto> getAll(Long userId, BookingState state, boolean isOwner) {
        userService.validationFindUserById(userId);
        List<Booking> bookingList;
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (isOwner) {
            switch (state) {
                case WAITING:
                    bookingList = bookingRepository.findAllByStatusAndItem_Owner_idOrderByStartDateDesc(BookingStatus.WAITING, userId);
                    break;
                case REJECTED:
                    bookingList = bookingRepository.findAllByStatusAndItem_Owner_idOrderByStartDateDesc(BookingStatus.REJECTED, userId);
                    break;
                case PAST:
                    bookingList = bookingRepository.findAllByEndDateBeforeAndItem_Owner_idOrderByStartDateDesc(currentDateTime, userId);
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findAllByStartDateAfterAndItem_Owner_idOrderByStartDateDesc(currentDateTime, userId);
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findAllByStartDateBeforeAndEndDateAfterAndItem_Owner_idOrderByStartDateDesc(
                            currentDateTime, currentDateTime, userId);
                    break;
                default:
                    bookingList = bookingRepository.findAllByItem_Owner_idOrderByStartDateDesc(userId);
                    break;
            }
        } else {
            switch (state) {
                case WAITING:
                    bookingList = bookingRepository.findAllByStatusAndBooker_idOrderByStartDateDesc(BookingStatus.WAITING, userId);
                    break;
                case REJECTED:
                    bookingList = bookingRepository.findAllByStatusAndBooker_idOrderByStartDateDesc(BookingStatus.REJECTED, userId);
                    break;
                case PAST:
                    bookingList = bookingRepository.findAllByEndDateBeforeAndBooker_idOrderByStartDateDesc(currentDateTime, userId);
                    break;
                case FUTURE:
                    bookingList = bookingRepository.findAllByStartDateAfterAndBooker_idOrderByStartDateDesc(currentDateTime, userId);
                    break;
                case CURRENT:
                    bookingList = bookingRepository.findAllByStartDateBeforeAndEndDateAfterAndBooker_idOrderByStartDateDesc(
                            currentDateTime, currentDateTime, userId);
                    break;
                default:
                    bookingList = bookingRepository.findAllByBooker_idOrderByStartDateDesc(userId);
                    break;
            }
        }

        return bookingMapper.toBookingDto(bookingList);
    }

    public Booking validateFindBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Not found booking %d", id)));
    }

    public void validationCreateBooking(User user, Item item, Booking booking) {
        if (!item.getAvailable()) {
            throw new NotValidRequestException("Объект не доступен");
        }
        if (!booking.getStartDate().isBefore(booking.getEndDate())) {
            throw new NotValidRequestException("Дата окончания должна быть больше даты начала");
        }
        if (item.getOwner().getId().equals(user.getId())) {
            throw new NotFoundException("Объект не доступен");
        }
        if (!bookingRepository.findAllIntersectionTime(item.getId(), booking.getStartDate(), booking.getEndDate()).isEmpty()) {
            throw new NotFoundException("Объект не найден");
        }
    }
}
