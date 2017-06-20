package com.dao;

/**
 * Created by hasee on 2017/5/8.
 */
import com.beans.*;
import com.JDBC.*;
import com.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class book_dao extends abstruct_dao{
    private book Book;

    public book_dao(book Book){
        super();
        this.Book=Book;
    }

    public book_dao(Connection conn,book Book){
        super(conn);
        this.Book=Book;
    }

    public ResultSet getResultSetByIsbn13(String isbn13){
        /*
        * 查询isbn13，返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = "select * from "+table_book+" where isbn13=\""+isbn13+"\";";
            ResultSet rs = stat.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResultSet getResultSetByRecommend_index(int _begin,int _end){
        /*
        * 查询isbn13，返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s limit ?,?;",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,_begin-1);
            ps.setInt(2,_end-1);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static book getBookByResultSet(ResultSet rs){
        /*
        * 根据结果集返回book
        * */
        try {
            book _Book = null;
            while(rs.next()) {
                _Book = new book();
                _Book.set_class(rs.getString(1));
                _Book.setSubclass(rs.getString(2));
                _Book.setImage(rs.getString(3));
                _Book.setTitle(rs.getString(4));
                String[] author = authorutil.string2Authorarr(rs.getString(5));
                _Book.setAuthor(author);
                _Book.setIsbn13(rs.getString(6));
                _Book.setIsbn10(rs.getString(7));
                _Book.setPreface(rs.getString(8));
                _Book.setContents(rs.getString(9));
                _Book.setPublisher(rs.getString(10));
                _Book.setVersion(rs.getString(11));
                _Book.setSummary(rs.getString(12));
            }
            return _Book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<book> getBooksByResultSet(ResultSet rs){
        /*
        * 根据结果集返回books 结果为vector
        * */
        try {
            List<book> Books= new Vector();
            while(rs.next()){
                book _Book = new book();
                _Book.set_class(rs.getString(1));
                _Book.setSubclass(rs.getString(2));
                _Book.setImage(rs.getString(3));
                _Book.setTitle(rs.getString(4));
                String[] author=authorutil.string2Authorarr(rs.getString(5));
                _Book.setAuthor(author);
                _Book.setIsbn13(rs.getString(6));
                _Book.setIsbn10(rs.getString(7));
                _Book.setPreface(rs.getString(8));
                _Book.setContents(rs.getString(9));
                _Book.setPublisher(rs.getString(10));
                _Book.setVersion(rs.getString(11));
                _Book.setSummary(rs.getString(12));
                Books.add(_Book);
            }
            return Books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExist(){
        /*
        * 判断一本书是否在book中存在
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = "select * from "+table_book+" where isbn13=\'"+Book.getIsbn13()+"\';";
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean isExistByIsbn13(String isbn13){
        book BookForIsbn13=new book();
        BookForIsbn13.setIsbn13(isbn13);
        book_dao BookForIsbn13_dao =new book_dao(BookForIsbn13);
        return BookForIsbn13_dao.isExist();
    }

    public boolean add_book(String first_class,String sub_class){
        /*
        * 在book中加入新书，输入为分类和子类
        * */
        try {
            String sql = String.format("insert into %s(class , subclass , cover , title , author , isbn13 , isbn10 ,preface , contents , press , version , introduction )values (?,?,?,?,?,?,?,?,?,?,?,?);", table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, first_class);
            ps.setString(2, sub_class);
            ps.setString(3, Book.getImage());
            ps.setString(4, Book.getTitle());

            String authorstring=authorutil.authorarr2String(Book.getAuthor());
            ps.setString(5, authorstring);
            ps.setString(6, Book.getIsbn13());
            ps.setString(7, Book.getIsbn10());
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setString(10,Book.getPublisher());
            ps.setString(11,"");
            ps.setString(12,Book.getSummary());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public book[] getRecommendedByIsbn13(int _begin,int _end){
        /*
        * 根据isbn13返回推荐的书目，
        * */
        try {
            String sql = String.format("select * from %s where isbn13 = \"%s\";", table_comment, Book.getIsbn13());
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            //ResultSet rs = ps.executeQuery();
            String _class,subclass;
            while (rs.next()){
                _class=rs.getString("class");
                subclass=rs.getString("subclass");
                break;
            }
            sql ="";
            List<book> books= getBooksByResultSet(rs);
            book[] array =new book[books.size()];
            return books.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean add(book Book){
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.add_book(Book.get_class(),Book.getSubclass());
    }

    public static book[] getRecommendedByIsbn13(String isbn13, int _begin,int _end){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.getRecommendedByIsbn13(_begin,_end);
    }

    public static book getBookByIsbn13(String isbn13){
        book_dao Book_dao= new book_dao(new book());
        return book_dao.getBookByResultSet(Book_dao.getResultSetByIsbn13(isbn13));
    }

    public static book[] getRecommendBook_index(int _begin,int _end){
        book_dao Book_dao= new book_dao(new book());
        List<book> books=getBooksByResultSet(Book_dao.getResultSetByRecommend_index(_begin,_end));
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public static book[] search(String key){
        /*
        * 查找书籍，按照isbn13，书名，书名首字母，书名拼音
        * */
        List<book> books = new Vector();
        books.clear();

        //isbn13
        book Book;
        Book=getBookByIsbn13(key);
        Book.setStorage(storage_book_dao.count_transcript(key));
        if (Book!=null) books.add(Book);


        book[] array =new book[books.size()];
        return books.toArray(array);
    }
}
