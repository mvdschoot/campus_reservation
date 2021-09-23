package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Food;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    @Query(value = "SELECT * FROM food", nativeQuery = true)
    public List<Food> getAllFood();

    @Query(value = "SELECT * FROM food WHERE id = :id", nativeQuery = true)
    public Food getFood(@Param("id") int id);

    @Query(value = "SELECT food.* FROM food INNER JOIN food_building ON "
            + "food.id = food_building.food_id WHERE building_id = :id",
            nativeQuery = true)
    public List<Food> getFoodByBuildingId(@Param("id") int id);

    @Query(value = "SELECT food.*, food_reservations.quantity FROM food INNER JOIN food_reservations"
            + " ON food.id = food_reservations.food_id WHERE reservation_id = :reservation", nativeQuery = true)
    public List<Food> getFoodByReservationId(@Param("reservation") int reservationId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO food_building(food_id, building_id) VALUES (:food, :building)", nativeQuery = true)
    public void addFoodToBuilding(@Param("food") int foodId, @Param("building") int buildingId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO food_reservations(reservation_id, food_id, quantity) "
            + "VALUES (:reservation, :food, :quantity)", nativeQuery = true)
    public void addFoodToReservation(@Param("reservation") int reservationId,
                                     @Param("food") int foodId, @Param("quantity") int quantity);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO food (name, price) VALUES (:name, :price)", nativeQuery = true)
    public void insertFood(@Param("name") String name, @Param("price") double price);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM food WHERE id = :id", nativeQuery = true)
    public void deleteFood(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM food_reservations WHERE reservation_id = :reservation AND food_id = :food",
            nativeQuery = true)
    public void deleteFoodReservation(@Param("reservation") int reservationId, @Param("food") int foodId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM food_building WHERE building_id = :building AND food_id = :food",
            nativeQuery = true)
    public void deleteFoodBuilding(@Param("building") int buildingId, @Param("food") int foodId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE food_reservations SET quantity = :quantity WHERE"
            + " reservation_id = :reservation AND food_id = :food", nativeQuery = true)
    public void updateFoodReservationQuantity(@Param("reservation") int reservationId,
                                              @Param("food") int foodId, @Param("quantity") int quantity);

    @Query(value = "SELECT * FROM food_reservations WHERE reservation_id = :reservation", nativeQuery = true)
    public List<Object[]> getFoodReservationByReservationId(@Param("reservation") int reservationId);

    @Query(value = "SELECT * FROM food_reservations", nativeQuery = true)
    public List<Object[]> getAllFoodReservations();

    @Modifying
    @Transactional
    @Query(value = "UPDATE food SET name = :name WHERE id = :id", nativeQuery = true)
    public void updateName(@Param("id") int id, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE food SET price = :price WHERE id = :id", nativeQuery = true)
    public void updatePrice(@Param("id") int id, @Param("price") double price);

}

