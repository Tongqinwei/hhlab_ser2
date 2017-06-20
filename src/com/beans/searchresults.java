package com.beans;

/**
 * Created by hasee on 2017/5/4.
 */
public class searchresults {
    private int count;
    private int start;
    private int total;
    private book[] books;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public book[] getBooks() {
        return books;
    }

    public void setBooks(book[] books) {
        this.books = books;
    }
}
