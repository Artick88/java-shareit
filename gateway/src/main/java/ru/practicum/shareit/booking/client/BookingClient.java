package ru.practicum.shareit.booking.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.baseclient.BaseClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.util.BookingState;
import ru.practicum.shareit.exception.exeption.NotValidRequestException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String BASE_PATH = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String url, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(url + BASE_PATH))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> create(long userId, BookingCreateDto bookingCreateDto) {
        return exchange("", HttpMethod.POST, bookingCreateDto, getHeaders(userId), null);
    }

    public ResponseEntity<Object> approveBooking(long userId, long bookingId, boolean approved) {
        Map<String, Object> params = Map.of("approved", approved);

        return exchange("/" + bookingId + "?approved={approved}", HttpMethod.PATCH, null,
                getHeaders(userId), params);
    }

    public ResponseEntity<Object> getById(long userId, long bookingId) {
        return exchange("/" + bookingId, HttpMethod.GET, null, getHeaders(userId), null);
    }


    public ResponseEntity<Object> getAll(long userId, BookingState state, boolean isOwner,
                                         int fromIndex, int size) {

        if (fromIndex < 0 || size < 1) {
            throw new NotValidRequestException("Bad parameter: from or size");
        }

        Map<String, Object> params = Map.of("state", state, "from", fromIndex, "size", size);

        String path = "?state={state}&from={from}&size={size}";

        if (isOwner) {
            path = "/owner" + path;
        }

        return exchange(path, HttpMethod.GET, null, getHeaders(userId), params);
    }
}
