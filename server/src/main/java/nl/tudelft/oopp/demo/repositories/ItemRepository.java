package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.Item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM calendarItems", nativeQuery = true)
    public List<Item> getAllItems();

    @Query(value = "SELECT * FROM calendarItems WHERE id = :id", nativeQuery = true)
    public Item getItem(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO calendarItems (user, title, date, starting_time, ending_time, description) "
            + "VALUES (:user, :title, :date, :starting_time, :ending_time, :description)", nativeQuery = true)
    public void insertItem(@Param("user") String user, @Param("title") String title,
                           @Param("date") String date, @Param("starting_time") String startingTime,
                           @Param("ending_time") String endingTime, @Param("description") String description);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM calendarItems WHERE id = :id", nativeQuery = true)
    public void deleteItem(@Param("id") int id);

    @Query(value = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES"
            + " WHERE TABLE_SCHEMA = 'OOPP38' AND TABLE_NAME = 'calendarItems'", nativeQuery = true)
    public int getCurrentId();

    @Query(value = "SELECT * FROM calendarItems WHERE user = :user", nativeQuery = true)
    public List<Item> getUserItems(@Param("user") String user);
}
