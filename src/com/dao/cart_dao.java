package com.dao;

import com.beans.*;
import com.util._math;

import java.awt.print.Book;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Created by hasee on 2017/5/27.
 */
public class cart_dao extends abstruct_dao{
    book_cart Book_cart = new book_cart();
    public cart_dao(String barcode,int userid){
        super();
        Book_cart.setIsbn13(_math.barcodeToIsbn13(barcode));
        Book_cart.setBarcode(barcode);
        Book_cart.setUserid(userid);
    }
    public cart_dao(storage_book Book, user User){
        super();
        Book_cart.setIsbn13(Book.getIsbn());
        Book_cart.setBarcode(Book.getBook_id());
        Book_cart.setUserid(User.getUserid());
    }
    public cart_dao(user User){
        super();
        Book_cart.setUserid(User.getUserid());
    }
    public cart_dao(int userid){
        super();
        Book_cart.setUserid(userid);
    }
    public ResultSet getuserscartToRs(){
        /*
        * 某个用户的购物车
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where userid=\"%d\";", table_cart, Book_cart.getUserid());
            ResultSet rs = stat.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isExist(){
        ResultSet rs=this.getuserscartToRs();
        try {
            while (rs.next()){
                if (rs.getString("barcode").equals(Book_cart.getBarcode())) return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public book_cart[] getUsersCart(){
        ResultSet rs=this.getuserscartToRs();
        try {
            List<book_cart> cart = new ArrayList<book_cart>() ;
            cart.clear();
            while (rs.next()){
                book_cart Book = new book_cart();
                Book.setUserid(rs.getInt("userid"));
                Book.setBarcode(rs.getString("barcode"));
                Book.setIsbn13(_math.barcodeToIsbn13(Book.getBarcode()));
                cart.add(Book);
            }
            book_cart[] array =new book_cart[cart.size()];
            return cart.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new book_cart[0];
    }

    public static book_cart[] getbooks_cart(int userid){
        if (!user_dao.isExistByUserid(userid)) {
            System.err.printf("The user whose id is \"%d\" is not exist!%n", userid);
            return new book_cart[0];
        }
        cart_dao cart = new cart_dao(userid);
        return cart.getUsersCart();
    }

    public static book_brief[] getbooks(int userid){
        if (!user_dao.isExistByUserid(userid)) {
            System.err.printf("The user whose id is \"%d\" is not exist!%n", userid);
            return new book_brief[0];
        }
        cart_dao cart = new cart_dao(userid);

        //购物车信息
        book_cart[] Book_carts=cart.getUsersCart();

        //转换为书籍信息
        book_dao Book_dao= new book_dao(new book());
        List<book_brief> books= new Vector();
        int len = Book_carts.length;
        for (int i=0;i<len;i++){
            book Book=book_dao.getBookByIsbn13(Book_carts[i].getIsbn13());
            books.add(new book_brief(Book,Book_carts[i].getBarcode()));
        }
        book_brief[] array =new book_brief[books.size()];
        return books.toArray(array);
    }

    public String add(boolean isWork){
        /*
        * 返回错误信息
        * */
        if (isExist()) {
            System.err.printf("The book \"%s\" is in your cart!%n", Book_cart.getIsbn13());
            return "The book is in your cart!";
        }
        if (storage_book_dao.getState(Book_cart.getBarcode())!=4){
            System.err.printf("The book \"%s\" has been borrowed!%n", Book_cart.getIsbn13());
            return "The book has been borrowed!";
        }
        try {
            if (isWork) abstruct_dao.work_begin();
            String sql = String.format("insert into %s(userid , barcode)values (?,?);", table_cart);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Book_cart.getUserid());
            ps.setString(2, Book_cart.getBarcode());
            ps.execute();
            if (isWork) abstruct_dao.work_commit();
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return "add success";
        }
    }

    public static String add(String barcode,int userid,boolean isWork){
        cart_dao cart = new cart_dao(barcode,userid);
        return cart.add(isWork);
    }

    public static String add(String barcode,String  unionid,boolean isWork){
        int userid = user_dao.getUserByUnionId(unionid).getUserid();
        cart_dao cart = new cart_dao(barcode,userid);
        return cart.add(isWork);
    }

    public String rid(boolean isWork){
        /*
        * 返回错误信息
        * */
        if (!isExist()) {
            System.err.printf("The book \"%s\" is in your cart!%n", Book_cart.getIsbn13());
            return "The book is not in your cart!";
        }
        try {
            if (isWork) abstruct_dao.work_begin();
            String sql = String.format("delete from %s where userid=? and barcode=? ;", table_cart);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Book_cart.getUserid());
            ps.setString(2, Book_cart.getBarcode());
            ps.execute();
            if (isWork) abstruct_dao.work_commit();
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return "delete success";
        }
    }

    public static String rid(String barcode,int userid,boolean isWork){
        cart_dao cart = new cart_dao(barcode,userid);
        return cart.rid(isWork);
    }
}
