package functions;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import database.DatabaseManagement;
import database.MovieCommentController;
import database.MovieInfoController;
import database.MovieLikeController;
import model.CommentList;
import model.Movie;
import model.People;
import org.checkerframework.checker.units.qual.A;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeyWordSearch implements HttpFunction {
    private static final Gson gson = new Gson();
    private static String[] parameterList = {"Title", "StarName", "DirectorName", "Limit", "Id", "LogicKey", "OrderKey"};
    private static MovieInfoController infoDatabase;
    private static MovieCommentController commentDatabase;
    private static MovieLikeController likeDatabase;

    public KeyWordSearch() throws SQLException, ClassNotFoundException, IOException {
        DatabaseManagement databaseManagement = new DatabaseManagement();
        infoDatabase = databaseManagement.getMovieInfoController();
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
                "\n[./moviesByKeyword]:" +
                "\n    parameters:[" + parameterList[0] + "],[" + parameterList[1] + "],[" + parameterList[2] + "],[" + parameterList[3] + "],[" + parameterList[5] + "],[" + parameterList[6] + "]" +
                "\n[./stars]:" +
                "\n    parameters:[" + parameterList[1] + "],[" + parameterList[3] + "]" +
                "\n[./starMovies]:" +
                "\n    parameters:[" + parameterList[4] + "],[" + parameterList[3] + "]" +
                "\n[./directors]:" +
                "\n    parameters:[" + parameterList[2] + "],[" + parameterList[3] + "]" +
                "\n[./directorMovies]:" +
                "\n    parameters:[" + parameterList[4] + "],[" + parameterList[3] + "]" +
                "\n[./movieInfo]:" +
                "\n    parameters:[" + parameterList[4] + "],[" + parameterList[3] + "]" +
                "\n[./moviesByCommentNum]:" +
                "\n    parameters:[" + parameterList[3] + "]" +
                "\n\nUrl info:\n" +
                getUrlInfo(httpRequest);

        if (pathList.length > 1) {
            Object[] parameterList = null;
            switch (pathList[1]) {
                case "moviesByKeyword":
                    parameterList = getParameters(parameters, new int[]{0, 1, 2, 3, 5, 6}, false);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = searchMovie(pList.get(0), pList.get(1), pList.get(2), gson.fromJson(pList.get(3), int[].class), pList.get(4), pList.get(5));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "stars":
                    parameterList = getParameters(parameters, new int[]{1, 3}, false);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getStars(pList.get(0), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "starMovies":
                    parameterList = getParameters(parameters, new int[]{4}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{4, 3}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getStarMovies(Integer.parseInt(pList.get(0)), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "directors":
                    parameterList = getParameters(parameters, new int[]{2, 3}, false);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getDirectors(pList.get(0), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "directorMovies":
                    parameterList = getParameters(parameters, new int[]{4}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{4, 3}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getDirectorMovies(Integer.parseInt(pList.get(0)), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "movieInfo":
                    parameterList = getParameters(parameters, new int[]{4}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        parameterList = getParameters(parameters, new int[]{4, 3}, false);
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        try {
                            backInfo = getMovieInfo(Integer.parseInt(pList.get(0)), gson.fromJson(pList.get(1), int[].class));
                        } catch (Exception e) {
                            backInfo = "[ERROR]: Wrong parameter format!\n\nUrl info:\n" + getUrlInfo(httpRequest)+
                                    "\n\n"+e.getMessage();
                        }
                    }
                    break;
                case "moviesByCommentNum":
                    parameterList = getParameters(parameters, new int[]{3}, true);
                    backInfo = (String) parameterList[0];
                    if (parameterList[0] == null) {
                        ArrayList<String> pList = (ArrayList<String>) parameterList[1];
                        backInfo = getMoviesByCommentNum(gson.fromJson(pList.get(0), int[].class));
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

    private String searchMovie(String title, String starName, String directorName, int[] limit, String logicKey, String orderKey) throws SQLException {
        if (title == null) {
            title = "";
        }
        if (starName == null) {
            starName = "";
        }
        if (directorName == null) {
            directorName = "";
        }
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        ArrayList<Movie> movies = infoDatabase.getMoviesByKeyword(title, starName, directorName, limit, logicKey, orderKey);
        return gson.toJson(movies);
    }

    private String getStars(String starName, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        ArrayList<People> stars = infoDatabase.getStarsByName(starName, limit);
        //for (int i = 0; i < stars.size(); i++) {
        //    int id = stars.get(i).getId();
        //    ArrayList<Movie> movies = infoDatabase.getMoviesByPeopleId(id, new int[]{0, 5});
        //    stars.get(i).setMovies(movies);
        //}
        return gson.toJson(stars);
    }

    private String getStarMovies(int id, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        String backInfo = "Can't find Star by [id] " + id;
        People star = infoDatabase.getStarById(id);
        if (star != null) {
            ArrayList<Movie> movies = infoDatabase.getMoviesByPeopleId(id, limit);
            for (int x=0;x<movies.size();x++)
            {
                int movieId = movies.get(x).getMovieId();
                movies.get(x).setLikes(likeDatabase.getNumberOfLikeByMovie(movieId));
            }
            star.setMovies(movies);
            backInfo = gson.toJson(star);
        }
        return backInfo;
    }

    private String getDirectors(String directorName, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        ArrayList<People> directors = infoDatabase.getDirectorsByName(directorName, limit);
        //for (int i = 0; i < directors.size(); i++) {
        //    int id = directors.get(i).getId();
        //    ArrayList<Movie> movies = infoDatabase.getMoviesByPeopleId(id, new int[]{0, 5});
        //    directors.get(i).setMovies(movies);
        //}
        return gson.toJson(directors);
    }

    private String getDirectorMovies(int id, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        String backInfo = "Can't find Director by [id] " + id;
        People director = infoDatabase.getDirectorById(id);
        if (director != null) {
            ArrayList<Movie> movies = infoDatabase.getMoviesByPeopleId(id, limit);
            for (int x=0;x<movies.size();x++)
            {
                int movieId = movies.get(x).getMovieId();
                movies.get(x).setLikes(likeDatabase.getNumberOfLikeByMovie(movieId));
            }
            director.setMovies(movies);
            backInfo = gson.toJson(director);
        }
        return backInfo;
    }

    private String getMovieInfo(int id, int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        Movie movie = infoDatabase.getAllMovieInfoById(id);
        String backInfo = "Can't find Movie by [id] " + id;
        if (movie != null) {
            CommentList commentList = commentDatabase.getCommentsByMovie(id, limit);
            movie.setCommentList(commentList);
            int numOfLike = likeDatabase.getNumberOfLikeByMovie(id);
            movie.setLikes(numOfLike);
            backInfo = gson.toJson(movie);
        }
        return backInfo;
    }

    public String getMoviesByCommentNum(int[] limit) throws SQLException {
        if (limit == null || limit.length != 2) {
            limit = new int[]{0, 10};
        } else {
            limit[1] = limit[1] - limit[0];
        }
        ArrayList<int[]> list = commentDatabase.getMovieIdByCommentNumber(limit);
        ArrayList<Movie> movieList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int movieId = list.get(i)[0];
            movieList.add(infoDatabase.getMovieInfoById(movieId));
        }
        return gson.toJson(movieList);
    }
}
