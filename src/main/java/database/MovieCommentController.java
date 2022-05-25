package database;

import model.Comment;
import model.CommentList;
import model.User;
import org.checkerframework.checker.units.qual.C;

import java.sql.*;

public class MovieCommentController {
    private static final String db_ip = "34.65.255.129";
    private static final String db_post = "3306";
    private static final String db_socketFactory = "com.google.cloud.sql.mysql.SocketFactory";
    private static final String db_cloudSqlInstance = "sem-demo-mk0:europe-west6:cloud-database";
    private static final String db_user = "root";
    private static final String db_password = "0811";
    private static final String db_name = "user_account_db";
    private final Connection connection;

    public MovieCommentController(String url_basic) throws SQLException {
        String url = String.format(url_basic, db_ip, db_post, db_name, db_cloudSqlInstance, db_socketFactory, db_user, db_password);
        this.connection = DriverManager.getConnection(url);
    }

    public void createTableMovieComment() throws SQLException {
        Statement statement = connection.createStatement();
        String executeSQL = "CREATE TABLE `MovieComment` (\n" +
                "  `id` int auto_increment,\n" +
                "  `userAccount` VARCHAR(50) NOT NULL,\n" +
                "  `movieId` int NOT NULL,\n" +
                "  `time` DATETIME NOT NULL,\n" +
                "  `text` VARCHAR(200) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  FOREIGN KEY (`userAccount`) REFERENCES `UserAccountInfo`(`Account`)\n" +
                "  );";
        statement.execute(executeSQL);
    }

    public Comment addComment(Comment comment) throws SQLException {
        String template = "insert into `MovieComment` (userAccount,movieId,time,text) values (\"%s\",\"%s\",\"%s\",\"%s\");";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, comment.getUserAccount(), comment.getMoveId(), comment.getCommentTimeString(), comment.getText());
        int result = statement.executeUpdate(executeSQL);
        if (result == 1) {
            return getCommentsByUserMovieTime(comment.getUserAccount(), comment.getMoveId(), comment.getCommentTimeString()).getByIndex(-1);
        }
        return null;
    }

    public Comment getCommentById(int id) throws SQLException {
        String template = "select * from `MovieComment` where `id` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        ResultSet result = statement.executeQuery(executeSQL);
        if (result.next()) {
            String userAccount = result.getString("userAccount");
            int movieId = result.getInt("movieId");
            String time = result.getString("time");
            String text = result.getString("text");
            return new Comment(id,time,movieId,userAccount,text);
        }
        return null;
    }

    public CommentList getCommentsByUser(String account,int[] limit) throws SQLException {
        CommentList commentList = new CommentList();
        String template = "select * from `MovieComment` " +
                "where `userAccount` = \"%s\" " +
                "limit %s,%s;";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, account, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("id");
            int movieId = result.getInt("movieId");
            String time = result.getString("time");
            String text = result.getString("text");
            commentList.add(new Comment(id,time,movieId,account,text));
        }
        return commentList;
    }

    public CommentList getCommentsByMovie(int movieId,int[] limit) throws SQLException {
        CommentList commentList = new CommentList();
        String template = "select * from `MovieComment` " +
                "where `movieId` = \"%s\" " +
                "limit %s,%s;";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, movieId, limit[0], limit[1]);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("id");
            String account = result.getString("userAccount");
            String time = result.getString("time");
            String text = result.getString("text");
            commentList.add(new Comment(id,time,movieId,account,text));
        }
        return commentList;
    }

    public CommentList getCommentsByUserMovieTime(String account,int movieId,String time) throws SQLException {
        CommentList commentList = new CommentList();
        String template = "select * from `MovieComment` " +
                "where `userAccount` = \"%s\" and `movieId` = \"%s\" and `time` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, account,movieId,time);
        ResultSet result = statement.executeQuery(executeSQL);
        while (result.next()) {
            int id = result.getInt("id");
            String text = result.getString("text");
            commentList.add(new Comment(id,time,movieId,account,text));
        }
        return commentList;
    }

    public void deleteCommentById(int id) throws SQLException {
        String template = "delete from `MovieComment` where `id` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template, id);
        statement.executeUpdate(executeSQL);
    }
}
