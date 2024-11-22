package ru.tokmakov.service.guest;

import ru.tokmakov.dto.comment.CommentDto;

import java.util.List;

public interface GuestCommentsService {
    List<CommentDto> findComments(Long eventId, int from, int size);
}
