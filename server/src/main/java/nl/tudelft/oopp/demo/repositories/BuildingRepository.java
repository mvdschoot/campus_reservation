package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Building;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query(value = "SELECT * FROM building", nativeQuery = true)
    public List<Building> getAllBuildings();

    @Query(value = "SELECT * FROM building WHERE id = :id", nativeQuery = true)
    public Building getBuilding(@Param("id") int id);

    @Query(value = "SELECT building.* FROM building INNER JOIN food_building ON"
            + " building.id = food_building.building_id WHERE food_building.food_id = :id", nativeQuery = true)
    public List<Building> getBuildingByFoodId(@Param("id") int foodId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO building (name, room_count, address, max_bikes, opening_time, closing_time)"
            + " VALUES (:name, :room_count, :address, :max_bikes, :opening_time, :closing_time)",
            nativeQuery = true)
    public void insertBuilding(@Param("name") String name, @Param("room_count") int roomCount,
                               @Param("address") String address, @Param("max_bikes") int maxBikes,
                               @Param("opening_time") String openingTime,
                               @Param("closing_time") String closingTime);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM building WHERE id = :id", nativeQuery = true)
    public void deleteBuilding(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE building SET name = :name WHERE id = :id", nativeQuery = true)
    public void updateName(@Param("id") int id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE building SET room_count = :room_count WHERE id = :id", nativeQuery = true)
    public void updateRoomCount(@Param("id") int id, @Param("room_count") int roomCount);

    @Modifying
    @Transactional
    @Query(value = "UPDATE building SET address = :address WHERE id = :id", nativeQuery = true)
    public void updateAddress(@Param("id") int id, @Param("address") String address);

    @Modifying
    @Transactional
    @Query(value = "UPDATE building SET max_bikes = :max_bikes WHERE id = :id", nativeQuery = true)
    public void updateMaxBikes(@Param("id") int id, @Param("max_bikes") int maxBikes);

    @Modifying
    @Transactional
    @Query(value = "UPDATE building SET opening_time = :opening_time, closing_time = :closing_time"
            + " WHERE id = :id", nativeQuery = true)
    public void updateOpeningHours(@Param("id") int id, @Param("opening_time") String openingTime,
                                   @Param("closing_time") String closingTime);
}
