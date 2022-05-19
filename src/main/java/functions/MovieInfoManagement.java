package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import database.DatabaseManagement;
import database.UserAccountInfoController;

import java.io.IOException;
import java.sql.SQLException;

public class MovieInfoManagement implements HttpFunction {

    private static final Gson gson = new Gson();
    //private static String[] parameterList = {"Account", "Password","NewPassword"};
    private static UserAccountInfoController database;

    public MovieInfoManagement() throws SQLException, ClassNotFoundException, IOException {
        database = new DatabaseManagement().getUserAccountInfoController();
    }

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {

    }
}
