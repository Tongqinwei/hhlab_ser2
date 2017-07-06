package com.beans;

/**
 * Created by hasee on 2017/5/4.
 */
public class book {
    private rate rating;//评分
    private String subtitle;
    private String[] author;
    private String pubdate;//出版时间
    private tag[] tags;//标签
    private String origin_title;
    private String image;//cover
    private String binding;//装订
    private String[] translator;
    private String catalog;//目录
    private String pages;
    private image_SLM images;
    private String alt;//url
    private String id;
    private String publisher;//press
    private String isbn10;
    private String isbn13;
    private String title;
    private String url;
    private String alt_title;
    private String author_intro;
    private String summary;//introduction
    private String price;

    private String ebook_url;
    private String ebook_price;
    private Series series;

    private String _class;
    private String subclass;
    private String preface;
    private String contents;
    private String version;

    private int storage;
    private int storage_cb;

    private String pinyin;

    public int getStorage_cb() {
        return storage_cb;
    }

    public void setStorage_cb(int storage_cb) {
        this.storage_cb = storage_cb;
    }

    public bookpage_bean toBookpage_bean(){
        bookpage_bean Bookpage_bean = new bookpage_bean();
        Bookpage_bean.set_class(_class);
        Bookpage_bean.setSubclass(subclass);
        Bookpage_bean.setRating(rating);
        Bookpage_bean.setAuthor(author);
        Bookpage_bean.setImage(image);
        Bookpage_bean.setPublisher(publisher);
        Bookpage_bean.setIsbn10(isbn10);
        Bookpage_bean.setIsbn13(isbn13);
        Bookpage_bean.setTitle(title);
        Bookpage_bean.setGuide_read(summary);
        Bookpage_bean.setStorage(storage);
        return Bookpage_bean;
    }

    public book_cart toBook_cart(int userid){
        book_cart Book_cart = new book_cart();
        Book_cart.setUserid(userid);
        Book_cart.setIsbn13(isbn13);
        return Book_cart;
    }

    public book(){}

    public String getEbook_url() {
        return ebook_url;
    }

    public void setEbook_url(String ebook_url) {
        this.ebook_url = ebook_url;
    }

    public String getEbook_price() {
        return ebook_price;
    }

    public void setEbook_price(String ebook_price) {
        this.ebook_price = ebook_price;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public rate getRating() {
        return rating;
    }

    public void setRating(rate rating) {
        this.rating = rating;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String[] getAuthor() {
        return author;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public tag[] getTags() {
        return tags;
    }

    public void setTags(tag[] tags) {
        this.tags = tags;
    }

    public String getOrigin_title() {
        return origin_title;
    }

    public void setOrigin_title(String origin_title) {
        this.origin_title = origin_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String[] getTranslator() {
        return translator;
    }

    public void setTranslator(String[] translator) {
        this.translator = translator;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public image_SLM getImages() {
        return images;
    }

    public void setImages(image_SLM images) {
        this.images = images;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlt_title() {
        return alt_title;
    }

    public void setAlt_title(String alt_title) {
        this.alt_title = alt_title;
    }

    public String getAuthor_intro() {
        return author_intro;
    }

    public void setAuthor_intro(String author_intro) {
        this.author_intro = author_intro;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public book_brief toBook_brief(String barcode){
        book_brief Book_brief =new book_brief();
        Book_brief.setImage(image);
        Book_brief.setIsbn13(isbn13);
        Book_brief.setTitle(title);
        Book_brief.setBarcode(barcode);
        return Book_brief;
    }
}
