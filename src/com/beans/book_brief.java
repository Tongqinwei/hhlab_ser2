package com.beans;

/**
 * Created by hasee on 2017/6/22.
 */
public class book_brief {
    /*
    * 书籍简要信息
    * */

    private String title;
    private String image;
    private String isbn13;
    private int storage;
    private int storage_cb;

    public book_brief(){

    }

    public book_brief(book Book){
        title=Book.getTitle();
        image=Book.getImage();
        isbn13=Book.getIsbn13();
        storage=Book.getStorage();
        storage_cb=Book.getStorage_cb();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getStorage_cb() {
        return storage_cb;
    }

    public void setStorage_cb(int storage_cb) {
        this.storage_cb = storage_cb;
    }
}
