package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
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
@Table(name = "food")
public class Food implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @JsonBackReference
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private Set<FoodReservations> foodReservations;

    @JsonBackReference
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    private Set<FoodBuilding> foodBuilding;

    public Food() {
    }

    /**
     * Constructor.
     *
     * @param id int
     * @param name String
     * @param price double
     */
    public Food(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.foodBuilding = new HashSet<>();
        this.foodReservations = new HashSet<>();
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
     * Retrieves the name of the food from the database.
     *
     * @return Returns the String name.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the price of the food from the database.
     *
     * @return Returns the double value price.
     */
    public double getPrice() {
        return price;
    }


    /**
     * Equals method.
     *
     * @param o An Object to be compared to "this".
     * @return True if o is the same object, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Food)) {
            return false;
        }

        Food temp = (Food)o;
        if (id != temp.getId()) {
            return false;
        }

        return true;
    }
}
