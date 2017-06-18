package club.lunchmates.lunchmates.data;

import java.util.Date;

/**
 * Event
 *
 * @author Implex1v
 */

public class Event {
    private int id;
    private String name;
    private String x;
    private String y;
    private int author;
    private Date date;

    public Event() {
        id = -1;
        name = "";
        x = "";
        y = "";
        author = 1;
        date = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
