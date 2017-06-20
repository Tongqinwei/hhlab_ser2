package com.dao;

import com.JDBC.JDBC;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hasee on 2017/5/19.
 */
public abstract class abstruct_dao {

    protected static final String database_hhlab ="hhlab";
    protected static final String table_book="book";
    protected static final String table_user="user";
    protected static final String table_book_mng="book_mng";
    protected static final String table_comment="comment";
    protected static final String table_book_brw="book_brw";
    protected static final String table_orderlist="orderlist";
    protected static final String table_ordertable="ordertable";
    protected static final String table_cart="cart";

    protected static Connection conn = null;
    public static Connection getConn(){
        return conn;
    }

    public static void setConn(Connection conn){
        abstruct_dao.conn=conn;
    }

    public abstruct_dao(){
        try {
            if (this.conn==null) conn= JDBC.getConnection();
            Statement stat = conn.createStatement();
            String sql = String.format("use %s;", database_hhlab);
            boolean flag=stat.execute(sql);
            if (flag==true) System.err.println("Can't find database \""+ database_hhlab +"\".");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstruct_dao(Connection conn){
        try {
            this.conn = conn;
            Statement stat = conn.createStatement();
            String sql = String.format("use %s;", database_hhlab);
            boolean flag=stat.execute(sql);
            if (flag==true) System.err.println("Can't find database \""+ database_hhlab +"\".");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void close(){
        if (conn!=null) JDBC.close(conn,null);
        conn=null;
    }

    public static void connect(){
        if (conn==null) conn= JDBC.getConnection();
    }
}
