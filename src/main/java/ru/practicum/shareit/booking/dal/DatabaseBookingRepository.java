package ru.practicum.shareit.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.status.BookingStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseBookingRepository extends BookingRepository, JpaRepository<Booking, Long> {
    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            ORDER BY boo.start DESC
            """)
    List<Booking> findAllBookingsForBooker(Long bookerId);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            ORDER BY boo.start DESC
            """)
    List<Booking> findAllBookingsForOwner(Long ownerId);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP BETWEEN boo.start AND boo.end
            ORDER BY boo.start DESC
            """)
    List<Booking> findCurrentBookingsForBooker(Long bookerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP BETWEEN boo.start AND boo.end
            ORDER BY boo.start DESC
            """)
    List<Booking> findCurrentBookingsForOwner(Long ownerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP > boo.end
            ORDER BY boo.start DESC
            """)
    List<Booking> findPastBookingsForBooker(Long bookerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP > boo.end
            ORDER BY boo.start DESC
            """)
    List<Booking> findPastBookingsForOwner(Long ownerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP < boo.start
            ORDER BY boo.start DESC
            """)
    List<Booking> findFutureBookingsForBooker(Long bookerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP < boo.start
            ORDER BY boo.start DESC
            """)
    List<Booking> findFutureBookingsForOwner(Long ownerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            AND
            boo.status = ?2
            ORDER BY boo.start DESC
            """)
    List<Booking> findWaitingBookingsForBooker(Long bookerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            AND
            boo.status = ?2
            ORDER BY boo.start DESC
            """)
    List<Booking> findWaitingBookingsForOwner(Long ownerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.booker.id = ?1
            AND
            boo.status = ?2
            ORDER BY boo.start DESC
            """)
    List<Booking> findRejectedBookingsForBooker(Long bookerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.owner.id = ?1
            AND
            boo.status = ?2
            ORDER BY boo.start DESC
            """)
    List<Booking> findRejectedBookingsForOwner(Long ownerId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP > boo.end
            ORDER BY boo.end DESC
            LIMIT 1
            """)
    Optional<Booking> findLastBookingForItem(Long itemId, BookingStatus status);

    @Override
    @Query("""
            SELECT boo
            FROM Booking AS boo
            WHERE boo.item.id = ?1
            AND
            boo.status = ?2
            AND
            CURRENT_TIMESTAMP < boo.start
            ORDER BY boo.start ASC
            LIMIT 1
            """)
    Optional<Booking> findNextBookingForItem(Long itemId, BookingStatus status);
}
