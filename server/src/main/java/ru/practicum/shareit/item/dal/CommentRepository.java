package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> findByItemId(Long itemId);
}
