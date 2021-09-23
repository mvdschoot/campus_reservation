package nl.tudelft.oopp.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "calenderItems")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user")
    private String user;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private String date;

    @Column(name = "startingTime")
    private String startingTime;

    @Column(name = "endingTime")
    private String endingTime;

    @Column(name = "description")
    private String description;

    public Item() {
    }

    /**
     * Constructor.
     *
     * @param id           //TODO
     * @param user         //TODO
     * @param title        //TODO
     * @param date         //TODO
     * @param startingTime //TODO
     * @param endingTime   //TODO
     * @param description  //TODO
     */
    public Item(int id, String user, String title, String date, String startingTime,
                String endingTime, String description) {

        this.id = id;
        this.user = user;
        this.title = title;
        this.date = date;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.description = description;
    }

    /**
     * Equals method.
     *
     * @param o object to get compared to
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        return getId() == item.getId();
    }

    /**
     * getter for the id field.
     *
     * @return id of item
     */
    public int getId() {
        return id;
    }

    /**
     * getter for the user field.
     *
     * @return user of item
     */
    public String getUser() {
        return user;
    }


    /**
     * getter for the title field.
     *
     * @return title of item
     */
    public String getTitle() {
        return title;
    }


    /**
     * getter for the date field.
     *
     * @return date of item
     */
    public String getDate() {
        return date;
    }


    /**
     * getter for the startingTime field.
     *
     * @return startingTime of item.
     */
    public String getStartingTime() {
        return startingTime;
    }


    /**
     * getter for the endingTime field.
     *
     * @return endingTime of item
     */
    public String getEndingTime() {
        return endingTime;
    }


    /**
     * getter for the description field.
     *
     * @return description of item
     */
    public String getDescription() {
        return description;
    }

}
