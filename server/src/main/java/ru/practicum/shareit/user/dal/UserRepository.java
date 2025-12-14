package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    void deleteById(Long id);
}
