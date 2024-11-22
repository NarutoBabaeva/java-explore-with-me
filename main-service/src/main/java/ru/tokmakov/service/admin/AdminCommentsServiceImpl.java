package ru.tokmakov.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.CommentMapper;
import ru.tokmakov.dto.comment.UpdateCommentAdminDto;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Comment;
import ru.tokmakov.repository.CommentRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCommentsServiceImpl implements AdminCommentsService {
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findComments(int from, int size) {
        log.info("Fetching comments with pagination: from={}, size={}", from, size);

        if (from < 0 || size <= 0) {
            log.error("Invalid pagination parameters: from={}, size={}", from, size);
            throw new IllegalArgumentException("Pagination parameters must be positive");
        }

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Comment> comments = commentRepository.findAll(pageable);

        log.info("Fetched {} comments", comments.getTotalElements());
        return comments.stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto findComment(Long commentId) {
        log.info("Fetching comment with id={}", commentId);

        Comment comment = getComment(commentId);

        log.info("Successfully fetched comment with id={}", commentId);
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long commentId, UpdateCommentAdminDto updateCommentAdminDto) {
        log.info("Updating comment with id={}", commentId);

        Comment comment = getComment(commentId);
        comment.setContent(updateCommentAdminDto.getContent());

        Comment updatedComment = commentRepository.save(comment);
        log.info("Successfully updated comment with id={}", commentId);

        return CommentMapper.toDto(updatedComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id={}", commentId);

        Comment comment = getComment(commentId);
        commentRepository.delete(comment);

        log.info("Successfully deleted comment with id={}", commentId);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("Comment with id={} not found", commentId);
            return new NotFoundException("Comment with id=" + commentId + " not found");
        });
    }
}