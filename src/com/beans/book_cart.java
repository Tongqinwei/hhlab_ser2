package com.beans;

/**
 * Created by hasee on 2017/5/27.
 */
public class book_cart {
    private String isbn13;
    private int userid;

    public book_cart(){}

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
