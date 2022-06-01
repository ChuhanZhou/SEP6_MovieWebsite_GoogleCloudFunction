package database;

import model.Movie;
import model.People;
import model.User;
import org.checkerframework.checker.units.qual.A;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MovieInfoController {
    private static final String db_ip = "34.65.255.129";
    private static final String db_post = "3306";
    private static final String db_socketFactory = "com.google.cloud.sql.mysql.SocketFactory";
    private static final String db_cloudSqlInstance = "sem-demo-mk0:europe-west6:cloud-database";
    private static final String db_user = "root";
    private static final String db_password = "0811";
    private static final String db_name = "sep1";
    private final Connection connection;

    public MovieInfoController(String url_basic) throws SQLException {
        String url = String.format(url_basic, db_ip, db_post, db_name, db_cloudSqlInstance, db_socketFactory, db_user, db_password);
        this.connection = DriverManager.getConnection(url);
    }

    public Movie getAllMovieInfoById(int id) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `id` = \"%s\";";
        Movie movie = null;
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("id");
            String title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");

            ArrayList<People> stars = getStarsByMovieId(id);
            ArrayList<People> directors = getDirectorsByMovieId(id);

            movie = new Movie(movieId, title, year, directors, stars, rating, votes);
        }

        return movie;
    }

    public Movie getMovieInfoById(int id) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `id` = \"%s\";";
        Movie movie = null;
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("id");
            String title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");

            movie = new Movie(movieId, title, year, rating, votes);
        }

        return movie;
    }

    public ArrayList<Movie> getMoviesInfoByIds(ArrayList<Integer> ids) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `id` in (%s);";
        ArrayList<Movie> movies = new ArrayList<>();
        if (ids.size()!=0)
        {
            Statement statement = connection.createStatement();
            String idsString = ids.toString().split("]")[0].split("\\[")[1];
            String executeSQL = String.format(template, idsString);
            ResultSet result = statement.executeQuery(executeSQL);
            while (result.next()) {
                int movieId = result.getInt("id");
                String title = result.getString("title");
                int year = result.getInt("year");
                double rating = result.getDouble("rating");
                int votes = result.getInt("votes");
                movies.add(new Movie(movieId, title, year, rating, votes));
            }
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByKeyword(String title, String starName, String directorName, int[] limit, String logicKey, String orderKey) throws SQLException {
        if (logicKey == null || !logicKey.equals("or")) {
            logicKey = "and";
        }
        if (orderKey == null) {
            orderKey = "rating";
        }
        switch (orderKey) {
            case "rating":
            case "year":
            case "votes":
                break;
            default:
                orderKey = "rating";
                break;
        }
        ArrayList<String> requirements = new ArrayList<>();
        String joinSQL = "";

        if (title != null && !title.equals("")) {
            String titleSearch = "`title` like \"%s\"";
            requirements.add(String.format(titleSearch, "%" + title + "%"));
        }
        if (starName != null && !starName.equals("")) {
            String starSearch = "p1.`name` like \"%s\"";
            requirements.add(String.format(starSearch, "%" + starName + "%"));
            joinSQL += "inner join `stars` on `movies`.id=`stars`.movie_id " +
                    "inner join `people` as p1 on `stars`.person_id=`p1`.id ";
        }
        if (directorName != null && !directorName.equals("")) {
            String directorSearch = "p2.`name` like \"%s\"";
            requirements.add(String.format(directorSearch, "%" + directorName + "%"));
            joinSQL += "inner join `directors` on `movies`.id=`directors`.movie_id " +
                    "inner join `people` as p2 on `directors`.person_id=`p2`.id ";
        }

        String keywordSQL = "";
        if (requirements.size() > 0) {
            keywordSQL = String.format("where %s ", requirements.get(0));
        }
        for (int i = 1; i < requirements.size(); i++) {
            keywordSQL += String.format("%s %s ", logicKey, requirements.get(i));
        }

        String template = "select movies.id,title,year,rating,votes from `movies`" +
                "%s" +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "%s" +
                "group by movies.id " +
                "order by %s desc " +
                "limit %s,%s;";
        ArrayList<Movie> movies = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, joinSQL,keywordSQL, orderKey, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        System.out.println(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("movies.id");
            title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");
            movies.add(new Movie(movieId, title, year, rating, votes));
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByPeopleId(int peopleId, int[] limit) throws SQLException {
        String template = "select id,title,year,rating,votes from `movies` " +
                "inner join `stars` on `movies`.id=`stars`.movie_id " +
                "inner join `directors` on `movies`.id=`directors`.movie_id " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `stars`.`person_id` = \"%s\" or `directors`.`person_id` = \"%s\" " +
                "group by id " +
                "order by rating desc " +
                "limit %s,%s;";
        ArrayList<Movie> movies = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, peopleId,peopleId, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("id");
            String title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");
            movies.add(new Movie(movieId, title, year, rating, votes));
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByStarId(int starId, int[] limit) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `stars` on `movies`.id=`stars`.movie_id " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `person_id` = \"%s\" " +
                "order by rating desc " +
                "limit %s,%s;";
        ArrayList<Movie> movies = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, starId, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("movie_id");
            String title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");
            movies.add(new Movie(movieId, title, year, rating, votes));
        }
        return movies;
    }

    public ArrayList<Movie> getMoviesByDirectorId(int directorId, int[] limit) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `directors` on `movies`.id=`directors`.movie_id " +
                "inner join `ratings` on `movies`.id=`ratings`.movie_id " +
                "where `person_id` = \"%s\" " +
                "order by rating desc " +
                "limit %s,%s;";
        ArrayList<Movie> movies = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, directorId, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int movieId = result.getInt("movie_id");
            String title = result.getString("title");
            int year = result.getInt("year");
            double rating = result.getDouble("rating");
            int votes = result.getInt("votes");
            movies.add(new Movie(movieId, title, year, rating, votes));
        }
        return movies;
    }

    public People getStarById(int id) throws SQLException {
        String template = "select people.id,name,birth from `people` " +
                "inner join `stars` on `people`.id=`stars`.person_id " +
                "where `people`.id = \"%s\" " +
                "group by people.id;";
        People star = null;
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            String name = result.getString("name");
            int birth = result.getInt("birth");
            star = new People(id, name, birth);
        }
        return star;
    }

    public ArrayList<People> getStarsByName(String keyword, int[] limit) throws SQLException {
        String template = "select people.id,name,birth from `people` " +
                "inner join `stars` on `people`.id=`stars`.person_id " +
                "where `name` like \"%s\" " +
                "group by people.id limit %s,%s;";
        ArrayList<People> stars = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, "%" + keyword + "%", limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("people.id");
            String name = result.getString("name");
            int birth = result.getInt("birth");
            stars.add(new People(id, name, birth));
        }
        return stars;
    }

    public ArrayList<People> getStarsByMovieId(int movieId) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `stars` on `movies`.id=`stars`.movie_id " +
                "inner join `people` on `people`.id=`stars`.person_id " +
                "where `movies`.id = \"%s\";";
        ArrayList<People> stars = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, movieId);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("person_id");
            String name = result.getString("name");
            int birth = result.getInt("birth");
            stars.add(new People(id, name, birth));
        }
        return stars;
    }

    public People getDirectorById(int id) throws SQLException {
        String template = "select people.id,name,birth from `people` " +
                "inner join `directors` on `people`.id=`directors`.person_id " +
                "where `people`.id = \"%s\" " +
                "group by people.id;";
        People star = null;
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            String name = result.getString("name");
            int birth = result.getInt("birth");
            star = new People(id, name, birth);
        }
        return star;
    }

    public ArrayList<People> getDirectorsByName(String keyword, int[] limit) throws SQLException {
        String template = "select people.id,name,birth from `people` " +
                "inner join `directors` on `people`.id=`directors`.person_id " +
                "where `name` like \"%s\" " +
                "group by people.id limit %s,%s;";
        ArrayList<People> directors = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, "%" + keyword + "%", limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("people.id");
            String name = result.getString("name");
            int birth = result.getInt("birth");
            directors.add(new People(id, name, birth));
        }
        return directors;
    }

    public ArrayList<People> getDirectorsByMovieId(int movieId) throws SQLException {
        String template = "select * from `movies` " +
                "inner join `directors` on `movies`.id=`directors`.movie_id " +
                "inner join `people` on `people`.id=`directors`.person_id " +
                "where `movies`.id = \"%s\";";
        ArrayList<People> directors = new ArrayList<>();
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, movieId);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("person_id");
            String name = result.getString("name");
            int birth = result.getInt("birth");
            directors.add(new People(id, name, birth));
        }
        return directors;
    }
}
