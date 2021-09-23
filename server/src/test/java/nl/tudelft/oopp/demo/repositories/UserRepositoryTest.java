package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import nl.tudelft.oopp.demo.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test Class that tests the repository for User.
 * It makes use of the Mockito framework.
 */
@SpringBootTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepo;

    private User u1;
    private User u2;
    private User u3;
    private List<User> userList;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        u1 = new User("user1", "passHASHED1", 0);
        u2 = new User("user2", "passHASHED2", 1);
        u3 = new User("user3", "passHASHED3", 2);
        userList = Arrays.asList(u1, u2, u3);
    }

    /**
     * Test for getAllUsersTest method.
     */
    @Test
    void getAllUsersTest() {
        when(userRepo.getAllUsers()).thenReturn(userList);
        assertEquals(userList, userRepo.getAllUsers());
    }

    /**
     * Test for getUser method.
     */
    @Test
    void getUserTest() {
        when(userRepo.getUser(anyString())).thenReturn(u1);
        assertEquals(u1, userRepo.getUser("user1"));
    }

    /**
     * Test for insertUser method.
     */
    @Test
    void insertUserTest() {
        userRepo.insertUser("test", "testHASHED", 0);
        verify(userRepo, times(1))
                .insertUser("test", "testHASHED", 0);
    }

    /**
     * Test for deleteUser method.
     */
    @Test
    void deleteUserTest() {
        userRepo.deleteUser("test");
        verify(userRepo, times(1)).deleteUser("test");
    }

    /**
     * Test for updatePassword method.
     */
    @Test
    void updatePasswordTest() {
        userRepo.updatePassword("userTest", "testPassHASHED");
        verify(userRepo, times(1))
                .updatePassword("userTest", "testPassHASHED");
    }

    /**
     * Test for updateType method.
     */
    @Test
    void updateTypeTest() {
        userRepo.updateType("userTest", 2);
        verify(userRepo, times(1))
                .updateType("userTest", 2);
    }
}