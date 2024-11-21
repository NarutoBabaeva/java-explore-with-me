package ru.tokmakov.controller.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.service.guest.GuestCommentsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments/{eventId}")
@RequiredArgsConstructor
public class GuestCommentsController {
    private final GuestCommentsService guestCommentsService;

    @GetMapping
    public List<CommentDto> findComments(@PathVariable Long eventId,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /comments/{} Parameters: from={}, size={}", eventId, from, size);

        List<CommentDto> comments = guestCommentsService.findComments(eventId, from, size);

        log.info("GET /comments/{} Successfully returned: {}", eventId, comments);
        return comments;
    }
}
