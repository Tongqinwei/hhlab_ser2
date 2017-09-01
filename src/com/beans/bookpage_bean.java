package com.beans;

/**
 * Created by hasee on 2017/5/18.
 */
public class bookpage_bean {
    private rate rating;//评分
    private String[] author;
    private String image;//cover
    private String publisher;//press
    private String isbn10;
    private String isbn13;
    private String title;
    private String guide_read;//introduction
    private String _class;
    private String subclass;
    private comment[] comments;
    private int storage;
    private storage_book[] storage_books;
    private int grade_times;
    private double grade_ave;

    public book toBook(){
        book Book = new book();
        Book.setRating(rating);
        Book.setAuthor(author);
        Book.setImage(image);
        Book.setPublisher(publisher);
        Book.setIsbn10(isbn10);
        Book.setIsbn13(isbn13);
        Book.setTitle(title);
        Book.setSummary(guide_read);
        Book.setStorage(storage);
        return Book;
    }

    public bookpage_bean(){};

    public rate getRating() {
        return rating;
    }

    public void setRating(rate rating) {
        this.rating = rating;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuide_read() {
        return guide_read;
    }

    public void setGuide_read(String guide_read) {
        this.guide_read = guide_read;
    }

    public comment[] getComments() {
        return comments;
    }

    public void setComments(comment[] comments) {
        this.comments = comments;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public storage_book[] getStorage_books() {
        return storage_books;
    }

    public void setStorage_books(storage_book[] storage_books) {
        this.storage_books = storage_books;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String getSubclass() {
        return subclass;
    }

    public void setSubclass(String subclass) {
        this.subclass = subclass;
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
}
