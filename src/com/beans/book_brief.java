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
    private String barcode;
    private String[] author;
    private int storage;
    private int storage_cb;
    private int grade_times;
    private double grade_ave;
    private String grade_ave_f;

    public book_brief(){

    }

    public book_brief(book Book){
        title=Book.getTitle();
        image=Book.getImage();
        isbn13=Book.getIsbn13();
        storage=Book.getStorage();
        storage_cb=Book.getStorage_cb();
    }

    public book_brief(book Book,String barcode){
        title=Book.getTitle();
        image=Book.getImage();
        isbn13=Book.getIsbn13();
        storage=Book.getStorage();
        storage_cb=Book.getStorage_cb();
        this.barcode=barcode;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public int getGrade_times() {
        return grade_times;
    }

    public void setGrade_times(int grade_times) {
        this.grade_times = grade_times;
    }

    public double getGrade_ave() {
        return grade_ave;
    }

    public void setGrade_ave(double grade_ave) {
        this.grade_ave = grade_ave;
    }

    public String getGrade_ave_f() {
        return grade_ave_f;
    }

    public void setGrade_ave_f(String grade_ave_f) {
        this.grade_ave_f = grade_ave_f;
    }
}
