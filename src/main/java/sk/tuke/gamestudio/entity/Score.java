package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQuery( name = "Score.getBestScores",
             query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.points DESC")


public class Score implements Serializable, Comparable<Score> {

    @Id
    @GeneratedValue
    private int ident; //identifikator

    private String player;
    private int points;

    @NotNull
    private String game;
    private Date playedOn;



    public Score() {
    }

    public Score(String username, int points, String game, Date playedOn) {
        this.player = username;
        this.points = points;
        this.game = game;
        this.playedOn = playedOn;
    }


    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }



    @Override
    public String toString() {
        return "Score{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", points=" + points +
                ", game='" + game + '\'' +
                ", playedOn=" + playedOn +
                '}';
    }

    @Override
    public int compareTo(Score o) {
        return 0;
    }
}
