package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Reservations;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReservationsRepository extends JpaRepository<Reservations, Long> {

    @Query(value = "SELECT * FROM reservations", nativeQuery = true)
    public List<Reservations> getAllReservations();

    @Query(value = "SELECT * FROM reservations WHERE id = :id", nativeQuery = true)
    public Reservations getReservation(@Param("id") int id);

    @Query(value = "SELECT * FROM reservations WHERE room = :id", nativeQuery = true)
    public List<Reservations> getReservationByRoom(@Param("id") int id);

    @Query(value = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES"
            + " WHERE TABLE_SCHEMA = 'OOPP38' AND TABLE_NAME = 'reservations'", nativeQuery = true)
    public int getCurrentId();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO reservations (username, room, date, starting_time, ending_time) "
            + "VALUES (LOWER(:username), :room, :date, :starting_time, :ending_time)", nativeQuery = true)
    public void insertReservation(@Param("username") String username,
                                  @Param("room") int room, @Param("date") String date,
                                  @Param("starting_time") String startingTime,
                                  @Param("ending_time") String endingTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM reservations WHERE id = :id", nativeQuery = true)
    public void deleteReservation(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET date = :date WHERE id = :id", nativeQuery = true)
    public void updateDate(@Param("id") int id, @Param("date") String date);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET starting_time = :starting_time WHERE id = :id", nativeQuery = true)
    public void updateStartingTime(@Param("id") int id, @Param("starting_time") String startingTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET ending_time = :ending_time WHERE id = :id", nativeQuery = true)
    public void updateEndingTime(@Param("id") int id, @Param("ending_time") String endingTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET username = :username WHERE id = :id", nativeQuery = true)
    public void updateUsername(@Param("id") int id, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE reservations SET room = :room WHERE id = :id", nativeQuery = true)
    public void updateRoom(@Param("id") int id, @Param("room") int room);

    @Query(value = "SELECT * FROM reservations WHERE username = LOWER(:username)", nativeQuery = true)
    public List<Reservations> getUserReservations(@Param("username") String username);
}
