package com.dao;

import com.beans.book;
import com.beans.storage_book;
import com.util.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/5/22.
 */
public class storage_book_dao extends abstruct_dao{
    private storage_book Storage_book;
    private static String[] stateString={"","borrowed","booked","reserved","available"};//空，借出，保留，可获得
    public storage_book_dao(storage_book Storage_book){
        super();
        this.Storage_book=Storage_book;
    }

    public storage_book_dao(Connection conn, storage_book Storage_book){
        super(conn);
        this.Storage_book=Storage_book;
    }

    public storage_book_dao(book Book, String Location){
        super();
        storage_book newst_book = new storage_book();
        newst_book.setIsbn(Book.getIsbn13());
        newst_book.setBook_location(Location);
        newst_book.setBook_state(num2State(4));
        this.Storage_book=newst_book;
    }

    private static String num2State(int key){
        if (key<=4&&key>=1) return stateString[key];
        else return "";
    }

    private static int state2Num(String state){
        for (int i=1;i<5;i++){
            if (state.equals(stateString[i])) return i;
        }
        return -1;//error;
    }

    public int count_transcript(){
        /*
        * 单纯的计数副本，不管是否可借
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = "select storage from "+table_book+" where isbn13=\'"+Storage_book.getIsbn()+"\';";
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            return rs.getInt("storage");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public storage_book[] getStorage_books(){
        /*
        * 返回userid 失败返回-1
        * */
        try {
            String sql = "select * from "+table_book_mng+" where isbn13 = ? ;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,Storage_book.getIsbn());
            ResultSet rs = ps.executeQuery();
            List<storage_book> storage_books= new ArrayList<storage_book>();
            storage_books.clear();
            while (rs.next()){
                storage_book newstorage_book = new storage_book();
                newstorage_book.setIsbn(rs.getString("isbn13"));
                newstorage_book.setBook_id(rs.getString("barcode"));
                newstorage_book.setBook_location(rs.getString("location"));
                newstorage_book.setBook_state(rs.getString("state"));
                storage_books.add(newstorage_book);
            }
            storage_book[] array =new storage_book[storage_books.size()];
            return storage_books.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean add_transcript(){
        /*
        * 在book_msg中加入副本,标记位默认为4
        * */
        if (!book_dao.isExistByIsbn13(Storage_book.getIsbn())) {
            System.err.println("The book "+Storage_book.getIsbn()+" was not in table "+table_book+".");
            return false;
        }
        int no=this.count_transcript()+1;
        String _no=_math.getSerial_number(no);
        try {
            String sql = String.format("insert into %s(isbn13 , barcode , location ,state )values (?,?,?,?);", table_book_mng);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Storage_book.getIsbn());
            ps.setString(2, Storage_book.getIsbn()+_no);
            ps.setString(3, Storage_book.getBook_location());
            ps.setInt(4, state2Num(Storage_book.getBook_state()));
            ps.execute();
            sql = String.format("update %s set storage=storage+1, storage_cb=storage_cb+1 where isbn13 = ?",table_book);
            ps = conn.prepareStatement(sql);
            ps.setString(1, Storage_book.getIsbn());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public static boolean add(book Book,String Location){
        storage_book_dao Storage_book_dao= new storage_book_dao(Book, Location);
        return Storage_book_dao.add_transcript();
    }
    public static boolean add(storage_book Storage_book){
        storage_book_dao Storage_book_dao= new storage_book_dao(Storage_book);
        return Storage_book_dao.add_transcript();
    }
    public static int count_transcript(String isbn13){
        storage_book Storage_book = new storage_book();
        Storage_book.setIsbn(isbn13);
        storage_book_dao Storage_book_dao = new storage_book_dao(Storage_book);
        return Storage_book_dao.count_transcript();
    }

    public static storage_book[] getStorage_books(String isbn13){
        storage_book Storage_book=new storage_book();
        Storage_book.setIsbn(isbn13);
        storage_book_dao Storage_book_dao = new storage_book_dao(Storage_book);
        return Storage_book_dao.getStorage_books();
    }
}
