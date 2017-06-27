package com.dao;

import com.beans.orderForm;
import com.beans.storage_book;
import com.util.OF_util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasee on 2017/6/26.
 */
public class orderForm_dao extends abstruct_dao {
    private orderForm OrderForm;
    public orderForm_dao(orderForm OrderForm){
        super();
        this.OrderForm=OrderForm;
    }
    public orderForm_dao(Connection conn, orderForm OrderForm){
        super(conn);
        this.OrderForm=OrderForm;
    }

    public boolean add(){
        /*
        * 将订单录入到数据库中，分两部分存储，ordertable存表头，book_brw存每一本书的信息
        * */
        boolean success=false;
        try {
            //开始事务
            work_begin();

            //在表ordertable中添加
            String sql = String.format("insert into %s( userid,orderid,ordertime,orderstate) values (?,?,?,?);", table_ordertable);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, OrderForm.getUserid());
            ps.setString(2, OrderForm.getOrderid());
            ps.setString(3, OrderForm.getOrdertime());
            ps.setInt(4, 1);
            ps.execute();

            //在表book_brw中添加
            for (int i=0;i<OrderForm.getBook_tot();i++){
                if (!book_brw_dao.addBorrrowList(OrderForm.getUnionid(),OrderForm.getOrderid(),OrderForm.getBooks()[i].getBook_id(),false)){
                    work_rollback();
                    return false;
                }
            }

            //提交事务
            work_commit();
            success=true;
        } catch (SQLException e) {
            //回滚事务
            work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public static boolean isOrderidExist(String orderid){
        abstruct_dao.connect();
        boolean success=false;
        try{
            String sql = String.format("select orderid from %s where orderid = ?;", table_ordertable);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,orderid);
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

    public boolean borrowAll(){
         /*
        * 将订单中所有书都借走
        * */
        //开始事务
        work_begin();

        //借书
        for (int i=0;i<OrderForm.getBook_tot();i++){
            if (!book_brw_dao.borrowBook(OrderForm.getUnionid(),OrderForm.getOrderid(),OrderForm.getBooks()[i].getBook_id(),false)){
                work_rollback();
                return false;
            }
        }

        //提交事务
        work_commit();
        return true;
    }

    public static orderForm getOrderForm (String orderid,boolean isWork){
        orderForm OrderForm = new orderForm();
        abstruct_dao.connect();
        try{
            String sql = String.format("select * from %s where orderid = ?;", table_ordertable);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,orderid);
            ResultSet rs = ps.executeQuery();            rs.last();
            if (rs.getRow()!=0) {
//                OF_util of_util =  new OF_util(user_dao,orderid);
//                storage_book book1 = new storage_book();
//                book1.setBook_id("9787020049295001");
//                of_util.add(book1);
//                storage_book book2 = new storage_book();
//                book2.setBook_id("9787208061644001");
//                of_util.add(book2);
//                OrderForm = of_util.toOrderForm();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return OrderForm;
        }


    }
}
