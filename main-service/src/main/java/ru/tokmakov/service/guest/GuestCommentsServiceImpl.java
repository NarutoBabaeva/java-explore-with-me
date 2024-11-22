package ru.tokmakov.service.guest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.tokmakov.dto.comment.CommentDto;
import ru.tokmakov.dto.comment.CommentMapper;
import ru.tokmakov.exception.NotFoundException;
import ru.tokmakov.model.Comment;
import ru.tokmakov.repository.CommentRepository;
import ru.tokmakov.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestCommentsServiceImpl implements GuestCommentsService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> findComments(Long eventId, int from, int size) {
        log.info("Fetching comments with pagination: from={}, size={}", from, size);

        eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " not found")
        );

        if (from < 0 || size <= 0) {
            log.error("Invalid pagination parameters: from={}, size={}", from, size);
            throw new IllegalArgumentException("Pagination parameters must be positive");
        }

        Pageable pageable = PageRequest.of(from / size, size);
        Page<Comment> comments = commentRepository.findByEventId(eventId, pageable);

        return comments.stream()
                .map(CommentMapper::toDto)
                .toList();
    }
}