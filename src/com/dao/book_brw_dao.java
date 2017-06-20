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
        try{
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql=String.format("insert into %s (barcode, orderid, borrowtime, mark) values(?,?,?,0)",table_book_mng);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Book_brw.getBarcode());
            ps.setString(2, Book_brw.getOrderid());
            ps.setString(3, time);
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public boolean isExist(){
        try{
            String sql = String.format("select * from %s where barcode = ? and orderid = ?;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,Book_brw.getBarcode());
            ps.setString(2,Book_brw.getOrderid());
            ResultSet rs = ps.executeQuery();
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public boolean markBorrow(){
        if (!isExist()) {
            System.err.println("the book is not in the table");
            return false;
        }
        try{
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql = String.format("update %s where barcode = ? and orderid = ? set returntime=? ,mark=1 ;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,Book_brw.getBarcode());
            ps.setString(2,Book_brw.getOrderid());
            ps.setString(3,time);
            ResultSet rs = ps.executeQuery();
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }
}
