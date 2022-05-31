package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import database.*;
import model.Comment;
import model.CommentList;
import model.Movie;
import model.User;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieInfoManagement implements HttpFunction {

    private static final Gson gson = new Gson();
    private static String[] parameterList = {"Account", "Password", "CommentId", "MovieId", "Text", "Limit"};
    private static UserAccountInfoController accountDatabase;
    private static MovieCommentController commentDatabase;
    private static MovieLikeController likeDatabase;
    private static MovieInfoController infoDatabase;

    public MovieInfoManagement() throws SQLException, ClassNotFoundException, IOException {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        accountDatabase = databaseManagement.getUserAccountInfoController();
        commentDatabase = databaseManagement.getMovieCommentController();
        likeDatabase = databaseManagement.getMovieLikeController();
        infoDatabase = databaseManagement.getMovieInfoController();
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
                "\n[./addComment]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[3] + "],[" + parameterList[4] + "]" +
                "\n[./getCommentsByUser]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[5] + "]" +
                "\n[./getCommentsByMovie]:" +
                "\n    parameters:[" + parameterList[3] + "],[" + parameterList[5] + "]" +
                "\n[./removeComment]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[2] + "]" +
                "\n[./changeLikeStatus]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[3] + "]" +
                "\n[./isLike]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[3] + "]" +
                "\n[./getLikeMovies]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[5] + "]" +
                "\n[./getLikeNum]:" +
                "\n    parameters:[" + parameterList[3] + "]" +
                "\n\nUrl info:\n" +
                getUrlInfo(httpRequest);

        if (pathList.length > 1) {
            Object[] parameterList = null;
            switch (pathList[1]) {
                case "addComment":
                    parameterList = getParameters(parameters, new int[]{0, 1, 3, 4}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = addComment(pList.get(0), pList.get(1), Integer.parseInt(pList.get(2)), pList.get(3));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "getCommentsByUser":
                    parameterList = getParameters(parameters, new int[]{0}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{0, 5}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getCommentsByUser(pList.get(0), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "getCommentsByMovie":
                    parameterList = getParameters(parameters, new int[]{3}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{3, 5}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getCommentsByMovie(Integer.parseInt(pList.get(0)), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "removeComment":
                    parameterList = getParameters(parameters, new int[]{0, 1, 2}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = removeComment(pList.get(0), pList.get(1), Integer.parseInt(pList.get(2)));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "changeLikeStatus":
                    parameterList = getParameters(parameters, new int[]{0, 1, 3}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = changeLikeStatus(pList.get(0), pList.get(1), Integer.parseInt(pList.get(2)));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "isLike":
                    parameterList = getParameters(parameters, new int[]{0, 1, 3}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = isLike(pList.get(0), pList.get(1), Integer.parseInt(pList.get(2)));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "getLikeMovies":
                    parameterList = getParameters(parameters, new int[]{0, 1}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{0, 1, 5}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getLikeMovies(pList.get(0), pList.get(1), gson.fromJson(pList.get(2), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "getLikeNum":
                    parameterList = getParameters(parameters, new int[]{3}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getLikeNum(Integer.parseInt(pList.get(0)));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
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

    private Object[] getParameters(Map<String, List<String>> parameters, int[] keyIndexList, boolean isAnd) {
        ArrayList<String> pList = new ArrayList<>();
        String info = null;
        if (isAnd) {
            for (int i = 0; i < keyIndexList.length; i++) {
                String key = parameterList[keyIndexList[i]];
                if (parameters.containsKey(key)) {
                    pList.add(parameters.get(key).get(0));
                } else {
                    info = "[ERROR]: Can't find parameter key [" + key + "]!";
                    break;
                }
            }
        } else {
            int i = 0, n = 0;
            info = "[ERROR]: Can't find parameter key ";
            for (; i < keyIndexList.length; i++) {
                String key = parameterList[keyIndexList[i]];
                if (parameters.containsKey(key)) {
                    pList.add(parameters.get(key).get(0));
                } else {
                    pList.add(null);
                    info += "[" + key + "]";
                    n++;
                }
            }
            if (i > n) {
                info = null;
            } else {
                info += "!";
            }
        }
        return new Object[]{info, pList};
    }

    private boolean checkPassword(String account, String password) throws SQLException {
        User user = accountDatabase.getUserAccountInfo(account);
        return user != null && user.checkPassword(password);
    }

    private String addComment(String account, String password, int movieId, String text) throws SQLException {
        String backInfo = "Wrong account or password!";
        if (checkPassword(account, password)) {
            Comment comment = new Comment(movieId, account, text);
            comment = commentDatabase.addComment(comment);
            return gson.toJson(comment);
        }
        return backInfo;
    }

    private String getCommentsByUser(String account, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 100};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        CommentList commentList = commentDatabase.getCommentsByUser(account, limit);
        return gson.toJson(commentList);
    }

    private String getCommentsByMovie(int movieId, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 100};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        CommentList commentList = commentDatabase.getCommentsByMovie(movieId, limit);
        return gson.toJson(commentList);
    }

    private String removeComment(String account, String password, int id) throws SQLException {
        Comment comment = commentDatabase.getCommentById(id);
        String backInfo = "Wrong comment id!";
        if (comment != null) {
            backInfo = "Wrong account or password!";
            if (comment.getUserAccount().equals(account) && checkPassword(account, password)) {
                commentDatabase.deleteCommentById(id);
                backInfo = "OK";
            }
        }
        return backInfo;
    }

    private String changeLikeStatus(String account, String password, int movieId) throws SQLException {
        String backInfo = "Wrong account or password!";
        if (checkPassword(account, password)) {
            if (likeDatabase.isLike(account, movieId)) {
                likeDatabase.removeLike(account, movieId);
            } else {
                likeDatabase.addLike(account, movieId);
            }
            backInfo = "OK";
        }
        return backInfo;
    }

    private String isLike(String account, String password, int movieId) throws SQLException {
        String backInfo = "Wrong account or password!";
        if (checkPassword(account, password)) {
            return String.valueOf(likeDatabase.isLike(account, movieId));
        }
        return backInfo;
    }

    private String getLikeMovies(String account, String password, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 100};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        String backInfo = "Wrong account or password!";
        if (checkPassword(account, password)) {
            ArrayList<Integer> movieIdList = likeDatabase.getMoviesIdOfLikeByUser(account);
            ArrayList<Movie> movieList = infoDatabase.getMoviesInfoByIds(movieIdList);
            return gson.toJson(movieList);
        }
        return backInfo;
    }

    private String getLikeNum(int movieId) throws SQLException {
        return String.valueOf(likeDatabase.getNumberOfLikeByMovie(movieId));
    }
}
