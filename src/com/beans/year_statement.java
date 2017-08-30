package com.beans;

/**
 * Created by hasee on 2017/8/26.
 */
public class year_statement {
    private String name;//姓名
    private int userid;//id
    private int books;//年度阅读量
    private String subclass;//年度阅读类别
    private double defeat;//占比

    public year_statement(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getBooks() {
        return books;
    }

    public void setBooks(int books) {
        this.books = books;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
    }

    public double getDefeat() {
        return defeat;
    }

    public void setDefeat(double defeat) {
        this.defeat = defeat;
    }
}
