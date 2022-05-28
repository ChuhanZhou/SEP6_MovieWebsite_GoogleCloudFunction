package database;

import java.sql.*;
import java.util.ArrayList;

public class MovieLikeController {
    private static final String db_ip = "34.65.255.129";
    private static final String db_post = "3306";
    private static final String db_socketFactory = "com.google.cloud.sql.mysql.SocketFactory";
    private static final String db_cloudSqlInstance = "sem-demo-mk0:europe-west6:cloud-database";
    private static final String db_user = "root";
    private static final String db_password = "0811";
    private static final String db_name = "user_account_db";
    private final Connection connection;

    public MovieLikeController(String url_basic) throws SQLException {
        String url = String.format(url_basic, db_ip, db_post, db_name, db_cloudSqlInstance, db_socketFactory, db_user, db_password);
        this.connection = DriverManager.getConnection(url);
    }

    public void createTableMovieLike() throws SQLException {
        Statement statement = connection.createStatement();
        String executeSQL = "CREATE TABLE `MovieLike` (\n" +
                "  `id` VARCHAR(50) NOT NULL,\n" +
                "  `userAccount` VARCHAR(50) NOT NULL,\n" +
                "  `movieId` int NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  FOREIGN KEY (`userAccount`) REFERENCES `UserAccountInfo`(`Account`)\n" +
                "  );";
        statement.execute(executeSQL);
    }

    public void addLike(String userAccount, int movieId) throws SQLException {
        String template = "insert into `MovieLike` (id,userAccount,movieId) values (\"%s\",\"%s\",\"%s\");";
        if (!isLike(userAccount, movieId)) {
            Statement statement = connection.createStatement();
            String executeSQL = String.format(template, userAccount + "_" + movieId, userAccount, movieId);
            statement.executeUpdate(executeSQL);
        }
    }

    public boolean isLike(String userAccount, int movieId) throws SQLException {
        String template = "select id from `MovieLike` where `id` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, userAccount + "_" + movieId);
        ResultSet result = statement.executeQuery(executeSQL);
        return result.next();
    }

    public int getNumberOfLikeByMovie(int movieId) throws SQLException {
        int numOfLike = 0;
        String template = "select id from `MovieLike` where `movieId` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, movieId);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            numOfLike++;
        }
        return numOfLike;
    }

    public ArrayList<Integer> getMoviesIdOfLikeByUser(String account) throws SQLException {
        ArrayList<Integer> moviesIdList = new ArrayList<>();
        String template = "select movieId from `MovieLike` where `userAccount` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, account);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            moviesIdList.add(result.getInt("movieId"));
        }
        return moviesIdList;
    }

    public void removeLike(String userAccount, int movieId) throws SQLException {
        String template = "delete from `MovieLike` where `id` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, userAccount + "_" + movieId);
        statement.executeUpdate(executeSQL);
    }

    public void removeLikeByUser(String userAccount) throws SQLException {
        String template = "delete from `MovieLike` where `userAccount` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, userAccount);
        statement.executeUpdate(executeSQL);
    }
}
