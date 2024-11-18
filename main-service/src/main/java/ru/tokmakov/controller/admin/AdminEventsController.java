package ru.tokmakov.controller.admin;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.event.EventState;
import ru.tokmakov.dto.event.UpdateEventAdminRequest;
import ru.tokmakov.service.admin.AdminEventsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.event.EventFullDto;

import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/admin/events")
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> findEvents(@RequestParam(required = false) Set<Long> users,
                                         @RequestParam(required = false) Set<EventState> states,
                                         @RequestParam(required = false) Set<Long> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @Min(0) @RequestParam(defaultValue = "0") Integer from,
                                         @Min(1) @RequestParam(defaultValue = "10") Integer size) {
        return adminEventsService.findEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @Validated @NotNull @RequestBody UpdateEventAdminRequest eventShortDto) {
        log.info("Received request to update event with ID: {}", eventId);

        EventFullDto eventFullDto = adminEventsService.updateEvent(eventId, eventShortDto);

        log.info("Event with ID: {} successfully updated", eventId);

        return eventFullDto;
    }
}