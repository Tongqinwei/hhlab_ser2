package com.beans;

/**
 * Created by hasee on 2017/5/19.
 */
public class storage_book {
    private String isbn;
    private String book_id;
    private String book_state;
    private String book_location;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getBook_state() {
        return book_state;
    }

    public void setBook_state(String book_state) {
        this.book_state = book_state;
    }

    public String getBook_location() {
        return book_location;
    }

    public void setBook_location(String book_location) {
        this.book_location = book_location;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
