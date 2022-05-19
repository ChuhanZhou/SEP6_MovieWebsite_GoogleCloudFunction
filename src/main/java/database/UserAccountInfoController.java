package database;

import model.User;

import java.sql.*;

public class UserAccountInfoController {

    private static final String db_ip = "34.65.255.129";
    private static final String db_post = "3306";
    private static final String db_socketFactory = "com.google.cloud.sql.mysql.SocketFactory";
    private static final String db_cloudSqlInstance = "sem-demo-mk0:europe-west6:cloud-database";
    private static final String db_user = "root";
    private static final String db_password = "0811";
    private static final String db_name = "user_account_db";
    private final Connection connection;

    public UserAccountInfoController(String url_basic) throws SQLException {
        String url = String.format(url_basic,db_ip,db_post,db_name,db_cloudSqlInstance,db_socketFactory,db_user,db_password);
        this.connection = DriverManager.getConnection(url);
    }

    private void createTableUserAccountInfo() throws SQLException {
        Statement statement = connection.createStatement();
        String executeSQL = "CREATE TABLE `UserAccountInfo` (\n" +
                "  `Account` VARCHAR(50) NOT NULL,\n" +
                "  `Password` VARCHAR(50) NOT NULL,\n" +
                "  PRIMARY KEY (`Account`)\n" +
                "  );";
        statement.execute(executeSQL);
    }

    public void createNewUser(String account, String password) throws SQLException {
        String template = "insert into `UserAccountInfo` (Account,Password) values (\"%s\",\"%s\");";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template,account,password);
        statement.executeUpdate(executeSQL);
    }

    public User getUserAccountInfo(String account) throws SQLException {
        String template = "select * from `UserAccountInfo` where `Account` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template,account);
        ResultSet result = statement.executeQuery(executeSQL);
        if (result.next()) {
            String password = result.getString("Password");
            return new User(account, password);
        }
        return null;
    }

    public boolean hasUserAccount(String account) throws SQLException {
        String template = "select `Account` from `UserAccountInfo` where `Account` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template,account);
        ResultSet result = statement.executeQuery(executeSQL);
        return result.getFetchSize()>0;
    }

    public void updateUserAccount(String account, String password) throws SQLException
    {
        String template = "update `UserAccountInfo` set `Password` = \"%s\" where `Account` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template,password,account);
        statement.executeUpdate(executeSQL);
    }

    public void deleteUserAccount(String account) throws SQLException
    {
        String template = "delete from `UserAccountInfo` where `Account` = \"%s\";";
        Statement statement = connection.createStatement();
        String executeSQL = String.format(template,account);
        statement.executeUpdate(executeSQL);
    }
}
