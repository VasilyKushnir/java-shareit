package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepository {
    User create(User user);

    Optional<User> read(Long id);

    User update(User user);

    void delete(Long id);

    Optional<User> findByEmail(String email);
}
