package ru.tokmakov.service.admin;

import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.UpdateCommentAdminDto;

import java.util.List;

public interface AdminCommentsService {
    List<CommentDto> findComments(int from, int size);

    CommentDto findComment(Long commentId);

    CommentDto updateComment(Long commentId, UpdateCommentAdminDto newCommentDto);

    void deleteComment(Long commentId);
}
