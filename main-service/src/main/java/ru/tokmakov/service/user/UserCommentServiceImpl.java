package ru.tokmakov.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.CommentMapper;
import ru.tokmakov.dto.comment.NewCommentDto;
import ru.tokmakov.exception.ForbiddenException;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Comment;
import ru.tokmakov.model.Event;
import ru.tokmakov.model.User;
import ru.tokmakov.repository.CommentRepository;
import ru.tokmakov.repository.EventRepository;
import ru.tokmakov.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommentServiceImpl implements UserCommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto saveComment(Long eventId, NewCommentDto newCommentDto) {
        log.info("Attempting to save a new comment for eventId: {}, userId: {}", eventId, newCommentDto.getUserId());

        Event event = getEvent(eventId);
        User user = getUser(newCommentDto.getUserId());
        Comment comment = CommentMapper.newCommentDtoToModel(newCommentDto, user, event);

        Comment savedComment = commentRepository.save(comment);

        log.info("Comment successfully saved with id: {}", savedComment.getId());
        return CommentMapper.toDto(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long eventId, int from, int size) {
        log.info("Fetching comments for eventId: {} with pagination - from: {}, size: {}", eventId, from, size);

        getEvent(eventId);

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Comment> comments = commentRepository.findByEventId(eventId, pageable);

        log.debug("Successfully fetched {} comments for eventId: {}", comments.getTotalElements(), eventId);

        return comments.stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long eventId, Long commentId, NewCommentDto newCommentDto) {
        log.info("Starting to update comment with id: {} for event with id: {}", commentId, eventId);

        getEvent(eventId);
        Comment oldComment = getComment(commentId);

        if (!oldComment.getUser().getId().equals(newCommentDto.getUserId())) {
            log.warn("User with id: {} is not authorized to update comment with id: {}", newCommentDto.getUserId(), oldComment.getId());
            throw new ForbiddenException("User with id= " + newCommentDto.getUserId() +
                                         " is not authorized to update comment with id=" + oldComment.getId());
        }

        updateCommentField(oldComment, newCommentDto);
        Comment updatedComment = commentRepository.save(oldComment);

        log.info("Successfully updated comment with id: {}", updatedComment.getId());

        return CommentMapper.toDto(updatedComment);
    }

    private void updateCommentField(Comment oldComment, NewCommentDto newComment) {
        oldComment.setContent(newComment.getContent());
    }

    @Override
    @Transactional
    public void deleteComment(Long eventId, Long commentId, Long userId) {
        log.info("Deleting comment with id: {} for event with id: {} by user with id: {}", commentId, eventId, userId);

        getEvent(eventId);
        Comment comment = getComment(commentId);
        getUser(userId);

        if (!comment.getUser().getId().equals(userId)) {
            log.warn("User with id: {} is not authorized to delete comment with id: {}", userId, commentId);
            throw new ForbiddenException("User with id " + userId +
                                         " is not authorized to delete comment with id " + commentId +
                                         " for event " + eventId);
        }

        commentRepository.delete(comment);
        log.info("Successfully deleted comment with id: {}", commentId);
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> {
            log.error("Event with id: {} not found", eventId);
            return new NotFoundException("Event with id=" + eventId + " not found");
        });
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id={} not found", userId);
            return new NotFoundException("User with id=" + userId + " not found");
        });
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("Comment with id={} not found", commentId);
            return new NotFoundException("Comment with id=" + commentId + " not found");
        });
    }
}
