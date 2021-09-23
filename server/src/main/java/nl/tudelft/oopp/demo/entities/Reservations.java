package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "room")
    private int room;

    @Column(name = "date")
    private String date;

    @Column(name = "starting_time")
    private String startingTime;

    @Column(name = "ending_time")
    private String endingTime;

    @JsonBackReference
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Set<FoodReservations> foodReservations;

    public Reservations() {
    }

    /**
     * Builder.
     *
     * @param id           int
     * @param username     String
     * @param date         String formatted like "". //TODO address format
     * @param startingTime String formatted like "". //TODO time format
     * @param endingTime   String formatted like "". //TODO time format
     */
    public Reservations(int id, String username, int room, String date, String startingTime, String endingTime) {
        this.id = id;
        this.username = username;
        this.room = room;
        this.date = date;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.foodReservations = new HashSet<>();
    }

    /**
     * Retrieves the int id from the database.
     *
     * @return Returns the int id.
     */
    public int getId() {
        return id;
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
     * Retrieves the int room from the database.
     *
     * @return Returns the int room.
     */
    public int getRoom() {
        return room;
    }

    /**
     * Retrieves the String date from the database.
     *
     * @return Returns the String date formatted like "". //TODO dateformat
     */
    public String getDate() {
        return date;
    }

    /**
     * Retrieves the String startingTime from the database.
     *
     * @return Returns the String startingTime formatted like "". //TODO time format
     */
    public String getStartingTime() {
        return startingTime;
    }

    /**
     * Retrieves the String endingTime from the database.
     *
     * @return Returns the String endingTime formatted like "". //TODO time format
     */
    public String getEndingTime() {
        return endingTime;
    }

    public Set<FoodReservations> getFoodReservations() {
        return foodReservations;
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

        Reservations reservation = (Reservations) o;

        return this.getId() == (reservation.getId());
    }

}
