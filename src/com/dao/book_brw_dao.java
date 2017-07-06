package com.dao;

import com.Reservation.ReservationManager;
import com.beans.book_brw;
import com.util._math;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public boolean addBorrowList(boolean isWork) {
        int state = storage_book_dao.getState(Book_brw.getBarcode());
        if (state==1){
                /*
                * 此书已被借走，发生异常
                * */
            System.err.println("The book has been borrowed.");
            return false;
        }
        boolean success=false;
        try{
            if (isWork) abstruct_dao.work_begin();
            String sql=String.format("insert into %s (barcode, orderid, mark) values(?,?,2);",table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Book_brw.getBarcode());
            ps.setString(2, Book_brw.getOrderid());
            ps.execute();
            //标记预定，自动修改库存。
            if (storage_book_dao.getState(Book_brw.getBarcode())!=4) {
                //书不为可借状态，回滚
                if (isWork) abstruct_dao.work_rollback();
                return false;
            }
            storage_book_dao.updateState(Book_brw.getBarcode(),2);
            if (isWork) abstruct_dao.work_commit();
            success=true;
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean markBorrow(boolean isWork) {
        if (!isExist()) {
            System.err.println("the (barcode,orderid) is not in the table");
            return false;
        }
        int state = storage_book_dao.getState(Book_brw.getBarcode());
        if (state==1){
                /*
                * 此书已被借走，发生异常
                * */
            System.err.println("The book has not been borrowed.");
            return false;
        }
        boolean success=false;
        try{
            if (isWork) abstruct_dao.work_begin();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql=String.format("update %s set borrowtime=? ,mark=0 where barcode = ? and orderid = ?;",table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,time);
            ps.setString(2,Book_brw.getBarcode());
            ps.setString(3,Book_brw.getOrderid());
            ps.execute();
            //标记被借，自动修改库存。
            storage_book_dao.updateState(Book_brw.getBarcode(),1);
            if (isWork) abstruct_dao.work_commit();
            success=true;
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
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

    public boolean markReturn(boolean isWork){
        boolean success=false;
        if (!isExist()) {
            System.err.println("the (barcode,orderid) is not in the table");
            return false;
        }
        int state = storage_book_dao.getState(Book_brw.getBarcode());
        if (state==4){
                /*
                * 此书未被借走，发生异常
                * */
            return false;
        }
        try{
            if (isWork) abstruct_dao.work_begin();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
            String time = dateFormat.format( now );
            String sql = String.format("update %s set returntime=? ,mark=1 where barcode = ? and orderid = ?;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,time);
            ps.setString(2,Book_brw.getBarcode());
            ps.setString(3,Book_brw.getOrderid());
            ps.execute();

            //标记被借，自动修改库存。
            storage_book_dao.updateState(Book_brw.getBarcode(),4);
            if (isWork) abstruct_dao.work_commit();
            success=true;
            // 启动线程，更新预订信息
            ReservationManager.update();
        }catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public static boolean addBorrrowList(String unionid, String orderid, String barcode,boolean isWork){
        book_brw Book_brw = new book_brw();
        Book_brw.setBarcode(barcode);
        Book_brw.setOrderid(orderid);
        book_brw_dao Book_brw_dao = new book_brw_dao(Book_brw);
        return Book_brw_dao.addBorrowList(isWork);
    }
    public static boolean borrowBook(String unionid, String orderid, String barcode,boolean isWork){
        book_brw Book_brw = new book_brw();
        Book_brw.setBarcode(barcode);
        Book_brw.setOrderid(orderid);
        book_brw_dao Book_brw_dao = new book_brw_dao(Book_brw);
        return Book_brw_dao.markBorrow(isWork);
    }

    public static boolean returnBook(String orderid, String barcode,boolean isWork){
        book_brw Book_brw = new book_brw();
        Book_brw.setBarcode(barcode);
        Book_brw.setOrderid(orderid);
        book_brw_dao Book_brw_dao = new book_brw_dao(Book_brw);
        return Book_brw_dao.markReturn(isWork);
    }

    public static book_brw[] getBook_brwsByOrderid(String orderid){
        abstruct_dao.connect();
        try {
            String sql = String.format("select * from %s where orderid = ? ;", table_book_brw);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,orderid);
            ResultSet rs = ps.executeQuery();
            List<book_brw> book_brws= new ArrayList<book_brw>();
            book_brws.clear();
            while (rs.next()){
                book_brw Book_brw = new book_brw();
                Book_brw.setOrderid(rs.getString("orderid"));
                Book_brw.setBarcode(rs.getString("barcode"));
                Book_brw.setBorrowtime(rs.getString("borrowtime"));
                Book_brw.setReturntime(rs.getString("returntime"));
                String isbn13= _math.barcodeToIsbn13(Book_brw.getBarcode());
                Book_brw.setBook(book_dao.getBookByIsbn13(isbn13).toBook_brief(Book_brw.getBarcode()));
                book_brws.add(Book_brw);
            }
            book_brw[] array =new book_brw[book_brws.size()];
            return book_brws.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
