package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "type")
    private int type;

    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<BikeReservation> bikeReservations;

    public User() {
    }

    /**
     * Builder.
     *
     * @param username String
     * @param password String (unencrypted)
     * @param type     int, 0 = admin, 1 = teacher, 2 = student //TODO check if correct
     */
    public User(String username, String password, int type) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    /**
     * Retrieves the String username from the database.
     *
     * @return Returns the String username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the String (unencrypted) password from the database.
     *
     * @return Returns the String password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the int type from the database.
     *
     * @return Returns the int type.
     */
    public int getType() {
        return type;
    }

    /**
     * equals.
     *
     * @param o The Object to compare to.
     * @return True if Object and "this" are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return this.username.equals(user.username);
    }

}


