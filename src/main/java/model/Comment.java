package model;

import org.checkerframework.checker.units.qual.C;

public class Comment {
    private String commentId;
    private String moveId;
    private String userAccount;
    private String subCommentId;
    private String text;

    public Comment(String commentId,String moveId,String userAccount,String subCommentId,String text)
    {
        this.commentId = commentId;
        this.moveId = moveId;
        this.userAccount = userAccount;
        this.subCommentId = subCommentId;
        this.text = text;
    }

    public Comment(String commentId,String moveId,String userAccount,String text)
    {
        this(commentId,moveId,userAccount,null,text);
    }

    public String getCommentId() {
        return commentId;
    }

    public String getMoveId() {
        return moveId;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public String getSubCommentId() {
        return subCommentId;
    }

    public String getText() {
        return text;
    }
}
