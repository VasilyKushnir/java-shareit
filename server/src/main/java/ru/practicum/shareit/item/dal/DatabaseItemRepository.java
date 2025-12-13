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
            it.owner.id = :id
            AND
            (it.name ilike concat('%',:text,'%')
            OR
            it.description ilike concat('%',:text,'%'))
            """)
    List<Item> searchItems(Long id, String text);
}
