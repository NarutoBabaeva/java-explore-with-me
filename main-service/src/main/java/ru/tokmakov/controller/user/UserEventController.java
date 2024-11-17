package ru.tokmakov.controller.user;

import org.springframework.validation.annotation.Validated;
import ru.tokmakov.dto.event.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.participation.ParticipationRequestDto;
import ru.tokmakov.service.user.UserEventService;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class UserEventController {
    private final UserEventService userEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> findEventsAddedByUser(@PathVariable Long userId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Received request to find events added by user with ID: {}. Pagination - from: {}, size: {}", userId, from, size);
        List<EventShortDto> eventShortDtos = userEventService.findEventsAddedByUser(userId, from, size);
        log.info("Found {} events for user with ID: {}", eventShortDtos.size(), userId);
        return eventShortDtos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto saveEvent(@PathVariable Long userId, @Validated @NotNull @RequestBody NewEventDto newEventDto) {
        log.info("Received request to create event for userId: {}, event data: {}", userId, newEventDto);
        EventFullDto savedEvent = userEventService.saveEvent(userId, newEventDto);
        log.info("Event successfully created for userId: {}, eventId: {}", userId, savedEvent.getId());
        return savedEvent;
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto findEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Received request to find event with ID: {} for user with ID: {}", eventId, userId);

        EventFullDto eventFullDto = userEventService.findEvent(userId, eventId);

        log.info("Returning full event details for event with ID: {}", eventId);
        return eventFullDto;
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @Validated @NotNull @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Request to update event received. UserId: {}, EventId: {}, UpdateData: {}", userId, eventId, updateEventUserRequest);
        EventFullDto event = userEventService.updateEvent(userId, eventId, updateEventUserRequest);
        log.info("Event updated successfully. UserId: {}, EventId: {}", userId, eventId);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> findParticipation(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Received request to find participation for userId={} and eventId={}", userId, eventId);
        List<ParticipationRequestDto> participationRequests = userEventService.findParticipation(userId, eventId);
        log.info("Successfully retrieved {} participation requests for userId={} and eventId={}", participationRequests.size(), userId, eventId);
        return participationRequests;
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateParticipation(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @Validated @NotNull @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("Received request to update participation for userId={} and eventId={} with status update request: {}",
                userId, eventId, eventRequestStatusUpdateRequest);
        EventRequestStatusUpdateResult result = userEventService.updateParticipation(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("Successfully updated participation for userId={} and eventId={}. " +
                 "Confirmed requests: {}, Rejected requests: {}",
                userId, eventId,
                result.getConfirmedRequests().size(),
                result.getRejectedRequests().size());
        return result;
    }
}