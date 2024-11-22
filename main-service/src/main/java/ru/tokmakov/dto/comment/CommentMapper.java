package ru.tokmakov.dto.comment;

import lombok.experimental.UtilityClass;
import ru.tokmakov.model.Comment;
import ru.tokmakov.model.Event;
import ru.tokmakov.model.User;

@UtilityClass
public class CommentMapper {
    public static Comment newCommentDtoToModel(NewCommentDto newCommentDto, User user, Event event) {
        Comment comment = new Comment();
        comment.setContent(newCommentDto.getContent());
        comment.setUser(user);
        comment.setEvent(event);
        return comment;
    }

    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setUserName(comment.getUser().getName());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedOn(comment.getCreatedOn());

        return commentDto;
    }
}