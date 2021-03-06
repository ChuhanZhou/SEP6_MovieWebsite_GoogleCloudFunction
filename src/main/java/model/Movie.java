package model;

import java.util.AbstractList;
import java.util.ArrayList;

public class Movie {
    private int movieId;
    private String title;
    private int year;
    private ArrayList<People> directors;
    private ArrayList<People> stars;
    private double rating;
    private int votes;

    private int likes;
    private CommentList commentList;

    public Movie(int movieId, String title, int year, ArrayList<People> directors, ArrayList<People> stars, double rating, int votes, int likes, CommentList commentList) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
        this.directors = directors;
        this.stars = stars;
        this.rating = rating;
        this.votes = votes;
        this.likes = likes;
        this.commentList = commentList;
    }

    public Movie(int movieId, String title, int year, ArrayList<People> directors, ArrayList<People> stars, double rating, int votes)
    {
        this(movieId,title,year,directors,stars,rating,votes,-1,null);
    }

    public Movie(int movieId, String title, int year, double rating, int votes)
    {
        this(movieId,title,year,null,null,rating,votes);
    }

    public Movie(int movieId, String title, int year)
    {
        this(movieId,title,year,-1,-1);
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public double getRating() {
        return rating;
    }

    public int getLikes() {
        return likes;
    }

    public int getVotes() {
        return votes;
    }

    public CommentList getCommentList() {
        return commentList;
    }

    public ArrayList<People> getDirectors() {
        return directors;
    }

    public ArrayList<People> getStars() {
        return stars;
    }

    public void setCommentList(CommentList commentList) {
        this.commentList = commentList;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDirectors(ArrayList<People> directors) {
        this.directors = directors;
    }

    public void setStars(ArrayList<People> stars) {
        this.stars = stars;
    }
}
