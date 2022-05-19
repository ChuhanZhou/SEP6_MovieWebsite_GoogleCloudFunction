package model;

import java.util.ArrayList;

public class People {

    private int id;
    private String name;
    private int birth;
    private ArrayList<Movie> movies;

    public People(int id, String name, int birth,ArrayList<Movie> movies)
    {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.movies = movies;
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

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
