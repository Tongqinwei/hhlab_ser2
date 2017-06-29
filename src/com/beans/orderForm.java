package com.beans;

/**
 * Created by hasee on 2017/6/26.
 */
public class orderForm {
    private int userid;
    private String unionid;
    private String orderid;
    private String ordertime;
    private int orderstate;
    private int book_tot;
    private book_brw[] books;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.ordertime = ordertime;
    }

    public int getOrderstate() {
        return orderstate;
    }

    public void setOrderstate(int orderstate) {
        this.orderstate = orderstate;
    }

    public int getBook_tot() {
        return book_tot;
    }

    public void setBook_tot(int book_tot) {
        this.book_tot = book_tot;
    }

    public book_brw[] getBooks() {
        return books;
    }

    public void setBooks(book_brw[] books) {
        this.books = books;
    }
}
