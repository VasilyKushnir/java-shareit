package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

@Repository
public interface DataBaseCommentRepository extends CommentRepository, JpaRepository<Comment, Long> {
}
