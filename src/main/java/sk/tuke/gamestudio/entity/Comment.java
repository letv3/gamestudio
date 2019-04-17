package sk.tuke.gamestudio.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQuery( name = "Comment.getComments",
             query = "SELECT s FROM Comment s WHERE s.game=:game ORDER BY s.commentedOn DESC")

public class Comment implements Serializable,  Comparable<Comment> {


    @Id
    @GeneratedValue
    private int ident;

    private String player;
    private String comment;
    @NotNull
    private String game;
    private Date commentedOn;

    public Comment() {}


    public Comment(String username, String comment, String gameName, Date commentedOn) {
        this.player = username;
        this.game = gameName;
        this.comment = comment;
        this.commentedOn = commentedOn;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    @Override
    public int compareTo(Comment o) {
        if(getCommentedOn() == null || o.getCommentedOn() == null) return 0;
        return getCommentedOn().compareTo(o.getCommentedOn());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ident=" + ident +
                ", player='" + player + '\'' +
                ", comment='" + comment + '\'' +
                ", game='" + game + '\'' +
                ", commentedOn=" + commentedOn +
                '}';
    }
}
