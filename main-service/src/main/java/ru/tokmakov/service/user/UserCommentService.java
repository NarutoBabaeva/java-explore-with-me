package ru.tokmakov.service.user;

import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.NewCommentDto;

import java.util.List;

public interface UserCommentService {
    CommentDto saveComment(Long eventId, NewCommentDto newCommentDto);

    List<CommentDto> getComments(Long eventId, int from, int size);

    CommentDto updateComment(Long eventId, Long commentId, NewCommentDto newCommentDto);

    void deleteComment(Long eventId, Long commentId, Long userId);
}
