package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQuery( name = "Rating.getRating",
            query = "SELECT s FROM Rating s WHERE s.game=:game AND s.player=:player")

public class Rating implements Serializable, Comparable<Rating>  {

    @Id
    private String player;


    private String game;

    private int rating;
    private Date ratedOn;


    public Rating() {}

    public Rating(String username, String gameName, int rating, Date ratedOn) {
        this.player = username;
        this.game = gameName;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    @Override
    public int compareTo(Rating o) {
        if(getRatedOn() == null || o.getRatedOn() == null) return 0;
        return getRatedOn().compareTo(o.getRatedOn());
    }

    @Override
    public String toString() {
        return "Rating{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }
}
