package model;


import com.google.type.DateTime;

import java.time.LocalDateTime;

public class Comment {
    private int commentId;
    private LocalDateTime commentTime;
    private int movieId;
    private String userAccount;
    private String text;

    public Comment(int commentId, LocalDateTime commentTime, int movieId, String userAccount, String text) {
        this.commentId = commentId;
        this.commentTime = commentTime;
        this.movieId = movieId;
        this.userAccount = userAccount;
        this.text = text;
    }

    public Comment(int commentId, String commentTimeString, int movieId, String userAccount, String text) {
        this.commentId = commentId;
        int yyyy = Integer.parseInt(commentTimeString.split(" ")[0].split("-")[0]);
        int mm = Integer.parseInt(commentTimeString.split(" ")[0].split("-")[1]);
        int dd = Integer.parseInt(commentTimeString.split(" ")[0].split("-")[2]);
        int h = Integer.parseInt(commentTimeString.split(" ")[1].split(":")[0]);
        int m = Integer.parseInt(commentTimeString.split(" ")[1].split(":")[1]);
        int s = Integer.parseInt(commentTimeString.split(" ")[1].split(":")[2]);
        this.commentTime = LocalDateTime.of(yyyy,mm,dd,h,m,s);
        this.movieId = movieId;
        this.userAccount = userAccount;
        this.text = text;
    }

    public Comment(int movieId, String userAccount, String text) {
        this(-1, LocalDateTime.now(), movieId, userAccount, text);
    }

    public int getCommentId() {
        return commentId;
    }

    public LocalDateTime getCommentTime() {
        return commentTime;
    }

    public String getCommentTimeString() {
        String basic = "%s-%s-%s %s:%s:%s";
        return String.format(basic, commentTime.getYear(), commentTime.getMonthValue(), commentTime.getDayOfMonth(), commentTime.getHour(), commentTime.getMinute(), commentTime.getSecond());
    }

    public int getMovieId() {
        return movieId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getText() {
        return text;
    }
}
