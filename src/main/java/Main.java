import com.google.gson.Gson;
import com.google.type.DateTime;
import database.*;
import model.Comment;
import model.Movie;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        MovieInfoController movieInfoController = databaseManagement.getMovieInfoController();
        UserAccountInfoController userAccountInfoController = databaseManagement.getUserAccountInfoController();
        MovieCommentController movieCommentController = databaseManagement.getMovieCommentController();
        MovieLikeController movieLikeController = databaseManagement.getMovieLikeController();
        Gson gson = new Gson();
        int[] a = new int[]{0,99};

        ArrayList<Movie> movies = movieInfoController.getMoviesByPeopleId(80,a);
        System.out.println(gson.toJson(movies));
    }
}
