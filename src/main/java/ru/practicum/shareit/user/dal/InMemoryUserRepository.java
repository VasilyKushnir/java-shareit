package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private Long id = 0L;
    private final Map<Long, User> userMap = new HashMap<>();

    @Override
    public User create(User user) {
        id++;
        userMap.put(id, user.toBuilder().id(id).build());
        return userMap.get(id);
    }

    @Override
    public Optional<User> read(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User update(User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    public Optional<User> findByEmail(String email) {
        return userMap.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
