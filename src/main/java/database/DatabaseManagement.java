package database;


import java.io.IOException;
import java.sql.*;

import org.postgresql.Driver;

public class DatabaseManagement {

    private static final String url_basic = "jdbc:mysql://%s:%s/%s?cloudSqlInstance=%s&socketFactory=%s&user=%s&password=%s";
    private final UserAccountInfoController userAccountInfoController;
    private final MovieInfoController movieInfoController;
    private final MovieCommentController movieCommentController;
    private final MovieLikeController movieLikeController;

    public DatabaseManagement() throws SQLException, ClassNotFoundException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        DriverManager.registerDriver(new Driver());

        userAccountInfoController = new UserAccountInfoController(url_basic);
        movieInfoController = new MovieInfoController(url_basic);
        movieCommentController = new MovieCommentController(url_basic);
        movieLikeController = new MovieLikeController(url_basic);
    }

    public UserAccountInfoController getUserAccountInfoController() {
        return userAccountInfoController;
    }

    public MovieInfoController getMovieInfoController() {
        return movieInfoController;
    }

    public MovieCommentController getMovieCommentController() {
        return movieCommentController;
    }

    public MovieLikeController getMovieLikeController() {
        return movieLikeController;
    }
}
