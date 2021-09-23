package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.BikeReservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface BikeReservationRepository extends JpaRepository<BikeReservation, Long> {
    @Query(value = "SELECT * FROM bike_reservation", nativeQuery = true)
    public List<BikeReservation> getAllBikeReservations();

    @Query(value = "SELECT * FROM bike_reservation WHERE id = :id", nativeQuery = true)
    public BikeReservation getBikeReservation(@Param("id") int id);

    @Query(value = "SELECT * FROM bike_reservation WHERE building = :building", nativeQuery = true)
    public List<BikeReservation> getBuildingBikeReservations(@Param("building") int building);

    @Query(value = "SELECT * FROM bike_reservation JOIN user ON bike_reservation.user = user.username "
            + "WHERE user.username = :username", nativeQuery = true)
    public List<BikeReservation> getUserBikeReservations(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bike_reservation (building, user, num_bikes, date, starting_time, ending_time)"
            + " VALUES (:building, :user, :numBikes, :date, :startingTime, :endingTime)", nativeQuery = true)
    public void insertBikeReservation(@Param("building") int building, @Param("user") String user,
                                      @Param("numBikes") int numBikes, @Param("date") String date,
                                      @Param("startingTime") String startingTime,
                                      @Param("endingTime") String endingTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM bike_reservation WHERE id = :id", nativeQuery = true)
    public void deleteBikeReservation(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET num_bikes = :numBikes WHERE id = :id", nativeQuery = true)
    public void updateBikeNum(@Param("id") int id, @Param("numBikes") int numBikes);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET building = :building WHERE id = :id", nativeQuery = true)
    public void updateBuilding(@Param("id") int id, @Param("building") int building);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET user = :username WHERE id = :id", nativeQuery = true)
    public void updateUser(@Param("id") int id, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET date = :date WHERE id = :id", nativeQuery = true)
    public void updateDate(@Param("id") int id, @Param("date") String date);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET starting_time = :startingTime WHERE id = :id", nativeQuery = true)
    public void updateStartingTime(@Param("id") int id, @Param("startingTime") String startingTime);

    @Modifying
    @Transactional
    @Query(value = "UPDATE bike_reservation SET ending_time = :endingTime WHERE id = :id", nativeQuery = true)
    public void updateEndingTime(@Param("id") int id, @Param("endingTime") String endingTime);
}
