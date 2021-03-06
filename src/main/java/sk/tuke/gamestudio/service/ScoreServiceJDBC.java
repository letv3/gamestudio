package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreServiceJDBC implements ScoreService {
    private static final String URL = "jdbc:postgresql://localhost/gamestudio";

    private static final String LOGIN = "postgres";

    private static final String PASSWORD = "36sas36";

    private static final String CREATE_COMMAND = "CREATE TABLE score (player VARCHAR(64) NOT NULL, points INTEGER NOT NULL, game VARCHAR(64) NOT NULL, played_on TIMESTAMP NOT NULL)";

    private static final String INSERT_COMMAND = "INSERT INTO score (player, points, game, played_on) VALUES (?, ?, ?, ?)";

    private static final String SELECT_COMMAND = "SELECT player, points, game, played_on FROM score WHERE game = ? ORDER BY points DESC";

    private static final String DELETE_COMMAND = "DELETE FROM score WHERE game=?";

    @Override
    public void addScore(Score score) throws ScoreException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(INSERT_COMMAND))
        {
            stmt.setString(1, score.getPlayer());
            stmt.setInt(2, score.getPoints());
            stmt.setString(3, score.getGame());
            stmt.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Score> getBestScores(String game) {
        List<Score> results = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SELECT_COMMAND))
        {
            stmt.setString(1, game);

            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Score score = new Score(
                            rs.getString(1), rs.getInt(2),
                            rs.getString(3), rs.getTimestamp(4));
                    results.add(score);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return results;
    }


    public void clearScores(String game) throws ScoreException{
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(DELETE_COMMAND)){
            stmt.setString(1, game);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
