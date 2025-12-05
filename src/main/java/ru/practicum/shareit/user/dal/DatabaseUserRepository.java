package ru.practicum.shareit.user.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@Repository
public interface DatabaseUserRepository extends UserRepository, JpaRepository<User, Long> {
    @Override
    @Query("""
            select us
            from User as us
            where us.email like %?1%
            """)
    Optional<User> findByEmail(String email);
}
