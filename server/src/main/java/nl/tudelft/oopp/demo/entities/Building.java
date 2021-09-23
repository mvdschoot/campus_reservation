package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
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
@Table(name = "building")
public class Building implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "roomCount")
    private int roomCount;

    @Column(name = "address")
    private String address;

    @Column(name = "openingTime")
    private String openingTime;

    @Column(name = "closingTime")
    private String closingTime;

    @Column(name = "maxBikes")
    private Integer maxBikes;

    @JsonBackReference
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL)
    private Set<FoodBuilding> foodBuilding;

    public Building() {
    }

    /**
     * Constructor with optional fields availableBikes and maxBikes.
     *
     * @param id          int
     * @param name        String
     * @param roomCount   int
     * @param address     String
     * @param maxBikes    int
     * @param openingTime  String
     * @param closingTime String
     */
    public Building(int id, String name, int roomCount, String address, int maxBikes,
                    String openingTime, String closingTime) {
        this.id = id;
        this.name = name;
        this.roomCount = roomCount;
        this.address = address;
        this.maxBikes = maxBikes;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.foodBuilding = new HashSet<>();
    }

    /**
     * Retrieves ID of the building from the database.
     *
     * @return Returns the int ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the name of the building from the database.
     *
     * @return Returns the String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the room count of the building from the database.
     *
     * @return Returns the int value roomCount.
     */
    public int getRoomCount() {
        return roomCount;
    }

    /**
     * Retrieves the address of the building from the database.
     *
     * @return Returns the String address formatted like: "".//TODO address format
     */
    public String getAddress() {
        return address;
    }


    /**
     * Retrieves the maximum amount of bikes for this building from the database.
     *
     * @return Returns the int value maxBikes
     */
    public int getMaxBikes() {
        return maxBikes;
    }

    /**
     * Retrieves the opening time for this building from the database.
     *
     * @return Returns the int value maxBikes
     */
    public String getOpeningTime() {
        return openingTime;
    }

    /**
     * Retrieves the closing time for this building from the database.
     *
     * @return Returns the int value maxBikes
     */
    public String getClosingTime() {
        return closingTime;
    }

    /**
     * Equals.
     *
     * @param o An Object to be compared to "this".
     * @return True if o is the same object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Building building = (Building) o;

        return this.id == building.id;
    }
}
