package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;


import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentServiceJDBC implements CommentService {

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";

    private static final String LOGIN = "postgres";

    private static final String PASSWORD = "36sas36";

    private static final String CREATE_COMMAND = "CREATE TABLE comment (player VARCHAR(64) NOT NULL, comment VARCHAR(64) NOT NULL, game VARCHAR(64) NOT NULL, commentedon TIMESTAMP NOT NULL)";

    private static final String INSERT_COMMAND = "INSERT INTO comment (player, comment, game, commentedon) VALUES (?, ?, ?, ?)";

    private static final String SELECT_COMMAND = "SELECT player, comment, game, commentedon FROM comment WHERE game = ? ";

    private static final String DELETE_COMMAND = "DELETE FROM comment WHERE game=?";




    @Override
    public void addComment(Comment comment) throws CommentException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(INSERT_COMMAND))
        {
            stmt.setString(1, comment.getPlayer());
            stmt.setString(2, comment.getComment());
            stmt.setString(3, comment.getGame());
            stmt.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        List<Comment> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SELECT_COMMAND))
        {
            stmt.setString(1, game);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getString(1),rs.getString(2),
                            rs.getString(3), rs.getTimestamp(4));
                    results.add(comment);
                }

                //results.sort(Comment::compareTo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(results);
        Collections.reverse(results);

        return results;


    }



    public void clearComments(String game) throws ScoreException{
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(DELETE_COMMAND)){
            stmt.setString(1, game);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
