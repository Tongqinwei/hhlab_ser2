package com.dao;

import com.beans.book;
import com.beans.book_cart;
import com.beans.storage_book;
import com.beans.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by hasee on 2017/5/27.
 */
public class cart_dao extends abstruct_dao{
    book_cart Book_cart = new book_cart();
    public cart_dao(String isbn13,int userid){
        super();
        Book_cart.setIsbn13(isbn13);
        Book_cart.setUserid(userid);
    }
    public cart_dao(book Book,user User){
        super();
        Book_cart.setIsbn13(Book.getIsbn13());
        Book_cart.setUserid(User.getUserid());
    }
    public cart_dao(storage_book Book, user User){
        super();
        Book_cart.setIsbn13(Book.getIsbn());
        Book_cart.setUserid(User.getUserid());
    }

    public ResultSet getuserscartToRs(){
        /*
        * 某个用户的购物车
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where userid=\"%d\";", Book_cart.getUserid(), table_cart);
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
                if (rs.getString("isbn13").equals(Book_cart.getIsbn13())) return true;
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
                Book.setIsbn13(rs.getString("isbn13"));
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
        cart_dao cart = new cart_dao(null,userid);
        return cart.getUsersCart();
    }

    public static book[] getbooks(int userid){
        if (!user_dao.isExistByUserid(userid)) {
            System.err.printf("The user whose id is \"%d\" is not exist!%n", userid);
            return new book[0];
        }
        cart_dao cart = new cart_dao(null,userid);

        //购物车信息
        book_cart[] Book_carts=cart.getUsersCart();

        //转换为书籍信息
        book_dao Book_dao= new book_dao(new book());
        List<book> books= new Vector();
        int len = Book_carts.length;
        for (int i=0;i<len;i++){
            book Book=book_dao.getBookByIsbn13(Book_carts[i].getIsbn13());
            books.add(Book);
        }
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public boolean add(){
        if (isExist()) {
            System.err.printf("The book \"%s\" is in your cart!%n", Book_cart.getIsbn13());
            return false;
        }
        book Book=book_dao.getBookByIsbn13(Book_cart.getIsbn13());
        try {
            String sql = String.format("insert into %s(userid , isbn13 , image , title)values (?,?,?,?);", table_cart);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Book_cart.getUserid());
            ps.setString(2, Book_cart.getIsbn13());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public static boolean add(String isbn13,int userid){
        cart_dao cart = new cart_dao(isbn13,userid);
        return cart.add();
    }

    public boolean rid(){
        try {
            String sql = String.format("delete * from table %s where userid=? and isbn13=? ;", table_cart);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Book_cart.getUserid());
            ps.setString(2, Book_cart.getIsbn13());
            ps.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return false;
        }
    }

    public static boolean rid(String isbn13,int userid){
        cart_dao cart = new cart_dao(isbn13,userid);
        return cart.rid();
    }
}
