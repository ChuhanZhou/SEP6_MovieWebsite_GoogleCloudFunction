package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        String url = String.format(url_basic,db_ip,db_post,db_name,db_cloudSqlInstance,db_socketFactory,db_user,db_password);
        this.connection = DriverManager.getConnection(url);
    }
}
