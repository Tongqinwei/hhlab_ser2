package com.dao;

import com.beans.book_brw;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasee on 2017/5/29.
 */
public class book_brw_dao extends abstruct_dao{
    book_brw Book_brw;
    public book_brw_dao(book_brw Book_brw){
        super();
        this.Book_brw=Book_brw;
    }
    public book_brw_dao(Connection conn,book_brw Book_brw){
        super(conn);
        this.Book_brw=Book_brw;
    }
    public boolean addBorrow() {
        int state = storage_book_dao.getState(Book_brw.getBarcode());
        if (state==1){
                /*
                * 此书已被借走，发生异常
                * */
            return false;
        }
        boolean success=false;
        try{
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql=String.format("insert into %s (barcode, orderid, borrowtime, mark) values(?,?,?,0)",table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Book_brw.getBarcode());
            ps.setString(2, Book_brw.getOrderid());
            ps.setString(3, time);
            ps.execute();
            //标记被借，自动修改库存。
            storage_book_dao.updateState(Book_brw.getBarcode(),1);
            success=true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean isExist(){
        boolean success=false;
        try{
            String sql = String.format("select * from %s where barcode = ? and orderid = ?;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,Book_brw.getBarcode());
            ps.setString(2,Book_brw.getOrderid());
            ResultSet rs = ps.executeQuery();
            rs.last();
            if (rs.getRow()!=0) success=true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean markReturn(){
        boolean success=false;
        if (!isExist()) {
            System.err.println("the book is not in the table");
            return false;
        }
        int state = storage_book_dao.getState(Book_brw.getBarcode());
        if (state!=1){
                /*
                * 此书未被借走，发生异常
                * */
            return false;
        }
        try{
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql = String.format("update %s set returntime=? ,mark=1 where barcode = ? and orderid = ?;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,time);
            ps.setString(2,Book_brw.getBarcode());
            ps.setString(3,Book_brw.getOrderid());
            ps.execute();

            //标记被借，自动修改库存。
            storage_book_dao.updateState(Book_brw.getBarcode(),4);

            success=true;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public static boolean borrowBook(String unionid, String orderid, String barcode){
        book_brw Book_brw = new book_brw();
        Book_brw.setBarcode(barcode);
        Book_brw.setOrderid(orderid);
        book_brw_dao Book_brw_dao = new book_brw_dao(Book_brw);
        return Book_brw_dao.addBorrow();
    }

    public static boolean returnBook(String orderid, String barcode){
        book_brw Book_brw = new book_brw();
        Book_brw.setBarcode(barcode);
        Book_brw.setOrderid(orderid);
        book_brw_dao Book_brw_dao = new book_brw_dao(Book_brw);
        return Book_brw_dao.markReturn();
    }
}
