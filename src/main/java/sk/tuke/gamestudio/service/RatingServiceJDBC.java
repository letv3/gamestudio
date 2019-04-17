package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import java.sql.*;

public class RatingServiceJDBC implements  RatingService{

    private static final String URL = "jdbc:postgresql://localhost/gamestudio";

    private static final String LOGIN = "postgres";

    private static final String PASSWORD = "36sas36";

    private static final String CREATE_COMMAND = "CREATE TABLE rating (player VARCHAR(64) NOT NULL,rating INTEGER NOT NULL, game VARCHAR(64) NOT NULL, ratedon TIMESTAMP NOT NULL)";

    private static final String INSERT_COMMAND = "INSERT INTO rating (player, rating, game, ratedon) VALUES (?, ?, ?, ?)";

    private static final String SELECT_COMMAND1 = "SELECT player, rating, game, ratedon FROM rating WHERE (player = ?, game = ?) ";

    //private static final String SELECT_COMMAND2 ="SELECT rating FROM rating WHERE ";



    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(INSERT_COMMAND))
        {
            stmt.setString(1, rating.getPlayer());
            stmt.setInt(2, rating.getRating());
            stmt.setString(3, rating.getGame());
            stmt.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return 0;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(SELECT_COMMAND1))
        {
            stmt.setString(1, player);
            stmt.setString(2, game);

            try(ResultSet rs = stmt.executeQuery()) {
               if(!rs.next()){
                    //int rating=rs.getInt(player);
                    return rs.getInt(player);
               }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
