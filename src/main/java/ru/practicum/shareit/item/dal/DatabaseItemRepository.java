package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface DatabaseItemRepository extends ItemRepository, JpaRepository<Item, Long> {
    @Override
    @Query("""
            select it
            from Item as it
            where it.available = true
            AND
            it.owner.id = ?1
            AND
            (lower(it.name) like lower(concat('%',?2,'%'))
            OR
            lower(it.description) like lower(concat('%',?2,'%')))
            """)
    List<Item> searchItems(Long id, String text);
}
