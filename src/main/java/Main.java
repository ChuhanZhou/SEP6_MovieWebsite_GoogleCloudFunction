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
        //Comment comment = new Comment(15414,"1","test text 1");
        //Comment comment1 = movieCommentController.addComment(comment);
        //System.out.println(gson.toJson(comment1));
        //movieLikeController.addLike("1",36606);
        //ArrayList<Movie> movies = movieInfoController.getMoviesInfoByIds(movieLikeController.getMoviesIdOfLikeByUser("1"));
        //int b=1;

        //String result = gson.toJson(movieInfoController.getMoviesByKeyword("","","Nesli",a));
        //String result = gson.toJson(movieInfoController.getMoviesByKeyword("ewe","","",a,"or"));
        //System.out.println(result);
    }
}
