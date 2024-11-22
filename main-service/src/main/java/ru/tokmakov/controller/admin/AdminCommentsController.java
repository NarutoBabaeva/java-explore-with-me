package ru.tokmakov.controller.admin;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.UpdateCommentAdminDto;
import ru.tokmakov.service.admin.AdminCommentsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @GetMapping
    public List<CommentDto> findComments(@RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("GET /admin/comments Parameters: from={}, size={}", from, size);

        List<CommentDto> comments = adminCommentsService.findComments(from, size);

        log.info("GET /admin/comments Successfully returned: {}", comments);
        return comments;
    }

    @GetMapping("{commentId}")
    public CommentDto findComment(@PathVariable Long commentId) {
        log.info("GET /admin/comments/{} getComment", commentId);

        CommentDto comment = adminCommentsService.findComment(commentId);

        log.info("GET /admin/comments/{} Successfully returned: {}", commentId, comment);
        return comment;
    }

    @PatchMapping("{commentId}")
    public CommentDto updateComment(@PathVariable Long commentId,
                                    @NotNull @Validated @RequestBody UpdateCommentAdminDto updateCommentAdminDto) {
        log.info("PATCH /admin/comments/{} updateComment", commentId);

        CommentDto updatedComment = adminCommentsService.updateComment(commentId, updateCommentAdminDto);

        log.info("PATCH /admin/comments/{} Successfully updated: {}", commentId, updatedComment);
        return updatedComment;
    }

    @DeleteMapping("{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long commentId) {
        log.info("DELETE /admin/comments/{} deleteComment", commentId);

        adminCommentsService.deleteComment(commentId);

        log.info("DELETE /admin/comments/{} Successfully deleted: {}", commentId, commentId);
    }
}