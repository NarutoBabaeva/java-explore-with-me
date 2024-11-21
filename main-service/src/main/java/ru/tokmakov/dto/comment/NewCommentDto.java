package ru.tokmakov.dto.comment;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class NewCommentDto {

    @NotNull
    private Long userId;

    @NotBlank(message = "Content must not be empty")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;
}
