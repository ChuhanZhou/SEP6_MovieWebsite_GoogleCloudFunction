import com.google.gson.Gson;
import database.DatabaseManagement;
import database.MovieInfoController;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        MovieInfoController movieInfoController = databaseManagement.getMovieInfoController();
        Gson gson = new Gson();
        int[] a = new int[]{0,99};
        //String result = gson.toJson(movieInfoController.getMoviesByKeyword("","","Nesli",a));
        String result = gson.toJson(movieInfoController.getMoviesByKeyword("ewe","","",a,"or"));
        System.out.println(result);
    }
}
