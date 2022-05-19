package model;

import java.util.ArrayList;

public class CommentList {
    private ArrayList<Comment> comments;

    public CommentList()
    {
        comments = new ArrayList<>();
    }

    public int getSize()
    {
        return comments.size();
    }

    public void add(Comment comment)
    {
        comments.add(comment);
    }

    public Comment getByIndex(int index)
    {
        if (index<comments.size())
        {
            return comments.get(index);
        }
        return null;
    }

    public void removeByIndex(int index)
    {
        if (index<comments.size())
        {
            comments.remove(index);
        }
    }
}
