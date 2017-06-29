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
    protected static final String table_admin="admin_table";

    protected static Connection conn = null;
    public static Connection getConn(){
        return conn;
    }

    public static void setConn(Connection conn){
        abstruct_dao.conn=conn;
    }

    public abstruct_dao(){
        try {
            if (conn==null) conn= JDBC.getConnection();
            Statement stat = conn.createStatement();
            String sql = String.format("use %s;", database_hhlab);
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Can't find database \""+ database_hhlab +"\".");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public abstruct_dao(Connection conn){
        try {
            conn = conn;
            Statement stat = conn.createStatement();
            String sql = String.format("use %s;", database_hhlab);
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Can't find database \""+ database_hhlab +"\".");
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

    public static void work_begin(){
        /*
        * 用于开启一个事务
        * */
        connect();
        try {
            Statement stat = conn.createStatement();
            String sql = "BEGIN ;";
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Cannot begin a work.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void work_rollback(){
        /*
        * 用于回滚一个事务
        * */
        connect();
        try {
            Statement stat = conn.createStatement();
            String sql = "ROLLBACK ;";
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Cannot rollback a work.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void work_commit(){
        /*
        * 用于提交一个事务
        * */
        connect();
        try {
            Statement stat = conn.createStatement();
            String sql = "COMMIT ;";
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Cannot commit a work.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void runSQL(String sql){
        connect();
        try {
            Statement stat = conn.createStatement();
            boolean flag=stat.execute(sql);
            if (flag) System.err.println("Cannot run the sql "+sql+" .");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void locktable(String tablename,boolean isReadLock){
        String lockname;
        if (isReadLock) lockname= "READ";
        else lockname="WRITE";
        String sql= String.format("LOCK TABLE %s %s", tablename, lockname);
        runSQL(sql);
        return ;
    }

    public static void unlock(String tablename){
        String sql= String.format("UNLOCK %s", tablename);
        runSQL(sql);
        return ;
    }
}
