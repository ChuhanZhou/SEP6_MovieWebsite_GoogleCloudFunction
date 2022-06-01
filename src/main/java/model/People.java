package model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class People {

    private int id;
    private String name;
    private int birth;
    private double avgRating;
    private int totalLike;
    private ArrayList<Movie> movies;

    public People(int id, String name, int birth,ArrayList<Movie> movies)
    {
        this.id = id;
        this.name = name;
        this.birth = birth;
        setMovies(movies);
    }

    public People(int id, String name, int birth)
    {
        this(id,name,birth,null);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBirth() {
        return birth;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
        avgRating = 0;
        totalLike = -1;
        if (movies!=null && movies.size()>0)
        {
            double total = 0;
            for (int i=0;i<movies.size();i++)
            {
                total+=movies.get(i).getRating();
                int likes = movies.get(i).getLikes();
                if (likes>=0)
                {
                    if (totalLike<0)
                    {
                        totalLike = 0;
                    }
                    totalLike += movies.get(i).getLikes();
                }
            }
            avgRating = Double.parseDouble(String.format("%.1f", total/movies.size()));
        }
    }
}
