package ru.tokmakov.controller.user;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.NewCommentDto;
import ru.tokmakov.service.user.UserCommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class UserCommentController {
    private final UserCommentService userCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto saveComment(@PathVariable Long eventId, @NotNull @Validated @RequestBody NewCommentDto newCommentDto) {
        log.info("POST /events/{}/comments - Creating new comment. Request data: {}", eventId, newCommentDto);

        CommentDto savedComment = userCommentService.saveComment(eventId, newCommentDto);

        log.info("POST /events/{}/comments - Comment created successfully. Saved comment: {}", eventId, savedComment);
        return savedComment;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getComments(@PathVariable Long eventId,
                                        @RequestParam(required = false, defaultValue = "0") int from,
                                        @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /events/{}/comments - Fetching comments, from={}, size={}", eventId, from, size);

        List<CommentDto> comments = userCommentService.getComments(eventId, from, size);

        log.info("GET /events/{}/comments - Found {} comments", eventId, comments.size());
        return comments;
    }

    @PatchMapping({"{commentId}"})
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long eventId, @PathVariable Long commentId,
                                    @NotNull @Validated @RequestBody NewCommentDto newCommentDto) {
        log.info("PATCH /events/{}/comments/{} - Updating comment. Request data: {}", eventId, commentId, newCommentDto);

        CommentDto updatedComment = userCommentService.updateComment(eventId, commentId, newCommentDto);

        log.info("PATCH /events/{}/comments/{} - Updating comment successfully. Updated comment:{}", eventId, commentId, updatedComment);
        return updatedComment;
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long eventId, @PathVariable Long commentId, @RequestParam Long userId) {
        log.info("DELETE /events/{}/comments/{} - Deleting comment. UserId={}", eventId, commentId, userId);

        userCommentService.deleteComment(eventId, commentId, userId);

        log.info("DELETE /events/{}/comments/{} - Deleted successfully", eventId, commentId);
    }
}