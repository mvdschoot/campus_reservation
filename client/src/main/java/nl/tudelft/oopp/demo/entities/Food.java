package nl.tudelft.oopp.demo.entities;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import nl.tudelft.oopp.demo.communication.FoodServerCommunication;

import org.json.JSONArray;
import org.json.JSONObject;


public class Food {

    private static Logger logger = Logger.getLogger("GlobalLogger");

    private IntegerProperty foodId;
    private StringProperty foodName;
    private DoubleProperty foodPrice;

    /**
     * Constructor to initialize without paramters.
     */
    public Food() {
        this.foodId = new SimpleIntegerProperty(-1);
        this.foodName = new SimpleStringProperty(null);
        this.foodPrice = new SimpleDoubleProperty(-1);
    }

    /**
     * Constructor to initialize with parameters.
     *
     * @param id    New Food id
     * @param name  New Food name
     * @param price New Food price
     */
    public Food(int id, String name, double price) {
        this.foodId = new SimpleIntegerProperty(id);
        this.foodName = new SimpleStringProperty(name);
        this.foodPrice = new SimpleDoubleProperty(price);
    }

    /**
     * Returns all foods in the database.
     *
     * @return List of Food
     */
    public static ObservableList<Food> getAllFoodData() {
        try {
            ObservableList<Food> foodData = FXCollections.observableArrayList();
            JSONArray jsonArrayFood = new JSONArray(FoodServerCommunication.getAllFood());
            for (int i = 0; i < jsonArrayFood.length(); i++) {
                Food f = new Food();
                f.setFoodId(jsonArrayFood.getJSONObject(i).getInt("id"));
                f.setFoodName(jsonArrayFood.getJSONObject(i).getString("name"));
                f.setFoodPrice(jsonArrayFood.getJSONObject(i).getDouble("price"));
                foodData.add(f);
            }
            return foodData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Returns all foods that are available at a particular building with ID.
     *
     * @param id The building ID
     * @return Returns a list of Food
     */
    public static ObservableList<Food> getFoodByBuildingId(int id) {
        try {
            ObservableList<Food> foodData = FXCollections.observableArrayList();
            JSONArray jsonArrayFood = new JSONArray(FoodServerCommunication.getFoodByBuildingId(id));
            for (int i = 0; i < jsonArrayFood.length(); i++) {
                Food f = new Food();
                f.setFoodId(jsonArrayFood.getJSONObject(i).getInt("id"));
                f.setFoodName(jsonArrayFood.getJSONObject(i).getString("name"));
                f.setFoodPrice(jsonArrayFood.getJSONObject(i).getDouble("price"));
                foodData.add(f);
            }
            return foodData;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Returns a Food that is associated with a particular ID.
     *
     * @param id The food ID
     * @return Returns a Food object
     */
    public static Food getFoodById(int id) {
        try {
            JSONObject jsonObject = new JSONObject(FoodServerCommunication.getFood(id));
            Food f = new Food();
            f.setFoodId(jsonObject.getInt("id"));
            f.setFoodName(jsonObject.getString("name"));
            f.setFoodPrice(jsonObject.getDouble("price"));
            return f;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    /**
     * Gets food ID.
     *
     * @return Returns ID
     */
    public IntegerProperty getFoodId() {
        return foodId;
    }

    /**
     * Sets the Food ID.
     *
     * @param foodId The new ID
     */
    public void setFoodId(int foodId) {
        this.foodId.set(foodId);
    }

    /**
     * Returns the food name.
     *
     * @return The name
     */
    public StringProperty getFoodName() {
        return foodName;
    }

    /**
     * Sets the food name.
     *
     * @param foodName The new name
     */
    public void setFoodName(String foodName) {
        this.foodName.set(foodName);
    }

    /**
     * Returns the food price.
     *
     * @return The price
     */
    public DoubleProperty getFoodPrice() {
        return foodPrice;
    }

    /**
     * Sets the food price.
     *
     * @param foodPrice The new price
     */
    public void setFoodPrice(double foodPrice) {
        this.foodPrice.set(foodPrice);
    }

    /**
     * The equals method of food.
     *
     * @param o food
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Food food = (Food) o;
        return foodId.get() == (food.getFoodId().get());
    }

}