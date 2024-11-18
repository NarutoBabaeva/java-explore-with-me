package ru.tokmakov.service.guest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.tokmakov.dto.HitDto;
import ru.tokmakov.dto.event.*;
import org.springframework.stereotype.Service;
import ru.tokmakov.exception.BadRequestException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Event;
import ru.tokmakov.repository.EventRepository;
import ru.tokmakov.stat.StatClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuestEventsServiceImpl implements GuestEventsService {
    private final EventRepository eventRepository;
    private final StatClient statClient;


    @Override
    public List<EventShortDto> findEvents(String text,
                                          List<Integer> categories,
                                          Boolean paid,
                                          String rangeStart,
                                          String rangeEnd,
                                          Boolean onlyAvailable,
                                          SortType sort,
                                          Integer from,
                                          Integer size,
                                          HttpServletRequest request) {
        LocalDateTime start = parseDateTime(rangeStart).orElse(null);
        LocalDateTime end = parseDateTime(rangeEnd).orElse(null);

        text = "%" + text + "%";

        String sortField = sort == SortType.VIEWS ? "views" : "eventDate";

        Sort sortBy = Sort.by(sortField);

        Pageable pageable = PageRequest.of(from / size, size, sortBy);

        Page<Event> events = start == null || end == null ?
                eventRepository.findEventsWithFiltersWithoutDate(text, categories, paid, onlyAvailable, LocalDateTime.now(), pageable)
                :
                eventRepository.findEventsWithFilters(text, categories, paid, start, end, onlyAvailable, pageable);

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        return events.stream()
                .map(EventMapper::toEventShortDto)
                .toList();
    }

    @Override
    public EventFullDto findEventById(long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id).orElse(null);

        if (event == null || event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + id + " not found");
        }

        if (!statClient.existsByIpAndUri(request.getRemoteAddr(), request.getRequestURI())) {
            event.setViews(event.getViews() + 1);
            event = eventRepository.save(event);
        }

        statClient.recordHit(new HitDto("ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        return EventMapper.toEventFullDto(event);
    }

    private Optional<LocalDateTime> parseDateTime(String dateTime) {
        try {
            return dateTime != null ? Optional.of(LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) : Optional.empty();
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Invalid date format: " + dateTime);
        }
    }
}