package ru.tokmakov.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UpdateCommentAdminDto {
    @NotBlank(message = "Content must not be empty")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;
}