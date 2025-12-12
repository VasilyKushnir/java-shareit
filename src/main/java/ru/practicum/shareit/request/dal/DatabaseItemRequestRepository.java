package ru.practicum.shareit.request.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

public interface DatabaseItemRequestRepository extends ItemRequestRepository, JpaRepository<ItemRequest, Long> {

}
