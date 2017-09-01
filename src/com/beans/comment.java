package com.beans;

import java.sql.Timestamp;

/**
 * Created by hasee on 2017/5/19.
 */
public class comment {
    private int id;
    private String name;
    private String user_name;
    private int userid;
    private String isbn13;
    private double rate;
    private String content;
    private Timestamp c_time;
    private user_brief2 User;
    private book_brief Book;

    public comment(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getC_time() {
        return c_time;
    }

    public void setC_time(Timestamp c_time) {
        this.c_time = c_time;
    }

    public user_brief2 getUser() {
        return User;
    }

    public void setUser(user_brief2 user) {
        User = user;
    }

    public book_brief getBook() {
        return Book;
    }

    public void setBook(book_brief book) {
        Book = book;
    }
}
