package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import database.DatabaseManagement;
import database.MovieCommentController;
import database.MovieLikeController;
import database.UserAccountInfoController;
import model.User;

public class UserAccountManagement implements HttpFunction {

    private static final Gson gson = new Gson();
    private static String[] parameterList = {"Account", "Password", "NewPassword"};
    private static UserAccountInfoController accountDatabase;
    private static MovieCommentController commentDatabase;
    private static MovieLikeController likeDatabase;

    public UserAccountManagement() throws SQLException, ClassNotFoundException, IOException {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        accountDatabase = databaseManagement.getUserAccountInfoController();
        commentDatabase = databaseManagement.getMovieCommentController();
        likeDatabase = databaseManagement.getMovieLikeController();
    }

    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
        httpResponse.appendHeader("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            httpResponse.appendHeader("Access-Control-Allow-Methods", "GET");
            httpResponse.appendHeader("Access-Control-Allow-Headers", "Content-Type");
            httpResponse.appendHeader("Access-Control-Max-Age", "3600");
            httpResponse.setStatusCode(HttpURLConnection.HTTP_NO_CONTENT);
            return;
        }

        Map<String, List<String>> parameters = httpRequest.getQueryParameters();

        String path = httpRequest.getPath();
        String[] pathList = path.split("/");
        String backInfo = "Path Function List:" +
                "\n[./register]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "]" +
                "\n[./login]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "]" +
                "\n[./changePassword]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[2] + "]" +
                "\n[./logOff]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "]" +
                "\n\nUrl info:\n" +
                getUrlInfo(httpRequest);

        if (pathList.length > 1) {
            Object[] parameterList = null;
            switch (pathList[1]) {
                case "register":
                    parameterList = getParameters(parameters, new int[]{0, 1});
                    backInfo = (String) parameterList[0];
                    if (backInfo == null) {
                        ArrayList<List<String>> pList = (ArrayList<List<String>>) parameterList[1];
                        backInfo = register(pList.get(0).get(0), pList.get(1).get(0));
                    }
                    break;
                case "login":
                    parameterList = getParameters(parameters, new int[]{0, 1});
                    backInfo = (String) parameterList[0];
                    if (backInfo == null) {
                        ArrayList<List<String>> pList = (ArrayList<List<String>>) parameterList[1];
                        backInfo = login(pList.get(0).get(0), pList.get(1).get(0));
                    }
                    break;
                case "changePassword":
                    parameterList = getParameters(parameters, new int[]{0, 1, 2});
                    backInfo = (String) parameterList[0];
                    if (backInfo == null) {
                        ArrayList<List<String>> pList = (ArrayList<List<String>>) parameterList[1];
                        backInfo = changePassword(pList.get(0).get(0), pList.get(1).get(0), pList.get(2).get(0));
                    }
                    break;
                case "logOff":
                    parameterList = getParameters(parameters, new int[]{0, 1});
                    backInfo = (String) parameterList[0];
                    if (backInfo == null) {
                        ArrayList<List<String>> pList = (ArrayList<List<String>>) parameterList[1];
                        backInfo = logOff(pList.get(0).get(0), pList.get(1).get(0));
                    }
                    break;
                default:
                    backInfo = "[ERROR]: Wrong function path!\n\n" + backInfo;
            }
        }

        BufferedWriter writer = httpResponse.getWriter();
        writer.write(backInfo);
    }

    private String getUrlInfo(HttpRequest httpRequest) {
        Map<String, List<String>> parameters = httpRequest.getQueryParameters();
        String method = httpRequest.getMethod();
        //Map<String, HttpRequest.HttpPart> parts = request.getParts();
        String[] path = httpRequest.getPath().split("/");
        //Optional<String> query = request.getQuery();
        String uri = httpRequest.getUri();

        String backInfo = "parameters: " + gson.toJson(parameters) +
                "\nmethod: " + method +
                "\npath: " + gson.toJson(path) +
                "\nuri: " + uri;
        return backInfo;
    }

    private Object[] getParameters(Map<String, List<String>> parameters, int[] keyIndexList) {
        ArrayList<List<String>> pList = new ArrayList<>();
        String info = null;
        for (int i = 0; i < keyIndexList.length; i++) {
            String key = parameterList[keyIndexList[i]];
            if (parameters.containsKey(key)) {
                pList.add(parameters.get(key));
            } else {
                info = "[ERROR]: Can't find parameter key [" + key + "]!";
                break;
            }
        }
        return new Object[]{info, pList};
    }

    private String register(String newAccount, String newPassword) throws SQLException {
        String backInfo = "The account [" + newAccount + "] has been used!";
        if (!accountDatabase.hasUserAccount(newAccount)) {
            accountDatabase.createNewUser(newAccount, newPassword);
            backInfo = "OK";
        }
        return backInfo;
    }

    private String login(String account, String password) throws SQLException {
        User user = accountDatabase.getUserAccountInfo(account);
        if (user != null && user.checkPassword(password)) {
            return "OK";
        }
        return "Wrong account or password!";
    }

    private String changePassword(String account, String oldPassword, String newPassword) throws SQLException {
        User user = accountDatabase.getUserAccountInfo(account);
        if (user != null && user.checkPassword(oldPassword)) {
            accountDatabase.updateUserAccount(account, newPassword);
            return "OK";
        }
        return "Wrong account or password!";
    }

    private String logOff(String account, String password) throws SQLException {
        User user = accountDatabase.getUserAccountInfo(account);
        if (user != null && user.checkPassword(password)) {
            commentDatabase.deleteCommentByUser(account);
            likeDatabase.removeLikeByUser(account);
            accountDatabase.deleteUserAccount(account);
            return "OK";
        }
        return "Wrong account or password!";
    }
}
