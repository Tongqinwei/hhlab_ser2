package com.beans;

/**
 * Created by hasee on 2017/8/30.
 */
public class home_bean {
    private user User;
    private int size;
    private book_brief[] books;
    private comment[] commments;

    public home_bean(){}

    public user getUser() {
        return User;
    }

    public void setUser(user user) {
        User = user;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public book_brief[] getBooks() {
        return books;
    }

    public void setBooks(book_brief[] books) {
        this.books = books;
    }

    public comment[] getCommments() {
        return commments;
    }

    public void setCommments(comment[] commments) {
        this.commments = commments;
    }
}
