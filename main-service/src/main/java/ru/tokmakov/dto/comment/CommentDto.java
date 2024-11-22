package ru.tokmakov.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;

    private Long userId;

    private String userName;

    private String content;

    private LocalDateTime createdOn;
}
