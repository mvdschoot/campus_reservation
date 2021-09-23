package nl.tudelft.oopp.demo.repositories;

import java.util.List;

import nl.tudelft.oopp.demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM user", nativeQuery = true)
    public List<User> getAllUsers();

    @Query(value = "SELECT * FROM user u WHERE u.username = LOWER(:username)", nativeQuery = true)
    public User getUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user (username, password, type) "
            + "VALUES (LOWER(:username), :password, :type)", nativeQuery = true)
    public void insertUser(@Param("username") String userName,
                           @Param("password") String password, @Param("type") int type);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE username = LOWER(:username)", nativeQuery = true)
    public void deleteUser(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET password = :password WHERE username = LOWER(:username)", nativeQuery = true)
    public void updatePassword(@Param("username") String username, @Param("password") String password);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user SET type = :type WHERE username = LOWER(:username)", nativeQuery = true)
    public void updateType(@Param("username") String username, @Param("type") int type);
}
