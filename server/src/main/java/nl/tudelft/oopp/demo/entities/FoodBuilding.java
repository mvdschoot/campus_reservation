package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FoodBuilding implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn
    @JsonManagedReference
    private Building building;

    /**
     * Default constructor.
     */
    public FoodBuilding() {
    }

    /**
     * Constructor to initialize the variables.
     *
     * @param food     The Food entity
     * @param building The Building entity
     */
    public FoodBuilding(Food food, Building building) {
        this.food = food;
        this.building = building;
    }

    /**
     * Returns Food entity.
     *
     * @return Returns Food entity
     */
    public Food getFood() {
        return food;
    }

    /**
     * Returns Building entity.
     *
     * @return Returns building entity
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * The equals method.
     *
     * @param o The object to compare 'this' to
     * @return Returns a boolean
     */
    public boolean equals(Object o) {
        if (!(o instanceof FoodBuilding)) {
            return false;
        }
        FoodBuilding temp = (FoodBuilding) o;
        if (food.getId() != temp.getFood().getId()) {
            return false;
        }
        if (building.getId() != temp.getBuilding().getId()) {
            return false;
        }
        return true;
    }

}

