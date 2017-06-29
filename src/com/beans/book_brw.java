package com.beans;

/**
 * Created by hasee on 2017/5/29.
 */
public class book_brw {
    private String barcode;
    private String orderid;
    private String borrowtime;
    private String returntime;
    private int mark;
    private book_brief book;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getBorrowtime() {
        return borrowtime;
    }

    public void setBorrowtime(String borrowtime) {
        this.borrowtime = borrowtime;
    }

    public String getReturntime() {
        return returntime;
    }

    public void setReturntime(String returntime) {
        this.returntime = returntime;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public book_brief getBook() {
        return book;
    }

    public void setBook(book_brief book) {
        this.book = book;
    }
}
