package com.dao;

import com.beans.orderForm;
import com.beans.storage_book;
import com.beans.user;
import com.util.OF_util;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public boolean add(boolean isWork){
        /*
        * 将订单录入到数据库中，分两部分存储，ordertable存表头，book_brw存每一本书的信息
        * */
        boolean success=false;
        try {
            //开始事务
            if (isWork) work_begin();

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
                if (!book_brw_dao.addBorrrowList(OrderForm.getUnionid(),OrderForm.getOrderid(),OrderForm.getBooks()[i].getBarcode(),false)){
                    if (isWork) work_rollback();
                    return false;
                }
            }

            //提交事务
            if (isWork) work_commit();
            success=true;
        } catch (SQLException e) {
            //回滚事务
            if (isWork) work_rollback();
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

    public boolean borrowAll(boolean isWork){
         /*
        * 将订单中所有书都借走
        * */
        //开始事务
        if (isWork) work_begin();

        //借书
        for (int i=0;i<OrderForm.getBook_tot();i++){
            if (!book_brw_dao.borrowBook(OrderForm.getUnionid(),OrderForm.getOrderid(),OrderForm.getBooks()[i].getBarcode(),false)){
                if (isWork) work_rollback();
                return false;
            }
        }

        //提交事务
        if (isWork) work_commit();
        return true;
    }

    public boolean returnAll(boolean isWork){
         /*
        * 将订单中所有书都归还
        * */
        //开始事务
        if (isWork) work_begin();

        //借书
        for (int i=0;i<OrderForm.getBook_tot();i++){
            if (!book_brw_dao.returnBook(OrderForm.getOrderid(),OrderForm.getBooks()[i].getBarcode(),false)){
                if (isWork) work_rollback();
                return false;
            }
        }

        //提交事务
        if (isWork) work_commit();
        return true;
    }

    public static orderForm getOrderFormByOrderid (String orderid){
        orderForm OrderForm = new orderForm();
        abstruct_dao.connect();
        try{
            String sql = String.format("select * from %s where orderid = ?;", table_ordertable);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,orderid);
            ResultSet rs = ps.executeQuery();
            rs.last();
            if (rs.getRow()!=0) {
                OrderForm.setUserid(rs.getInt("userid"));
                OrderForm.setOrderid(rs.getString("orderid"));
                user User=user_dao.getUserByUserid(OrderForm.getUserid());
                if (User!=null) OrderForm.setUnionid(User.getUnionid());
                OrderForm.setOrdertime(rs.getString("ordertime"));
                OrderForm.setOrderstate(rs.getInt("orderstate"));
                OrderForm.setBooks(book_brw_dao.getBook_brwsByOrderid(OrderForm.getOrderid()));
                OrderForm.setBook_tot(OrderForm.getBooks().length);
                return OrderForm;
            }else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static orderForm[] getOrderFormsByuserid (int userid){
        abstruct_dao.connect();
        List<orderForm> OrderForms= new ArrayList<orderForm>();
        OrderForms.clear();
        try{
            String sql = String.format("select * from %s where userid = ?;", table_ordertable);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                orderForm OrderForm=new orderForm();
                OrderForm.setUserid(rs.getInt("userid"));
                OrderForm.setOrderid(rs.getString("orderid"));
                user User=user_dao.getUserByUserid(OrderForm.getUserid());
                if (User!=null) OrderForm.setUnionid(User.getUnionid());
                OrderForm.setOrdertime(rs.getString("ordertime"));
                OrderForm.setOrderstate(rs.getInt("orderstate"));
                OrderForm.setBooks(book_brw_dao.getBook_brwsByOrderid(OrderForm.getOrderid()));
                OrderForm.setBook_tot(OrderForm.getBooks().length);
                OrderForms.add(OrderForm);
            }
            orderForm[] array =new orderForm[OrderForms.size()];
            return OrderForms.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static orderForm[] getOrderFormsByUnionid (String unionid){
        abstruct_dao.connect();
        int userid=user_dao.isExistByUnionid(unionid);
        if (userid>=1) return getOrderFormsByuserid(userid);
        else return new orderForm[0];
    }
    private boolean changeState(int newState,boolean isWork){
        /*
        * 支持的状态改变方式
        * 1->2 用户找管理员确认，状态从未确认变为已确认，其他什么都不做
        * 2->3 用户付款，状态从已确认变为已付款,同时，借出所有书籍
        * 3->4 用户还所有书，状态从正在进行变为已完成，归还所有书籍
        * 1->5 用户下单后未确认，状态变为失效，归还所有书籍
        * 2->5 用户下单后未付款，状态变为失效，归还所有书籍
        * */
        boolean success=false;
        int lastState=OrderForm.getOrderstate();
        String orderid=OrderForm.getOrderid();
        abstruct_dao.connect();
        String sql;
        boolean ifreturnbook=false;
        boolean ifborrowbook=false;
        if (lastState==1&&newState==2){
            sql = String.format("update %s set orderstate=2 where orderid = %s;", table_ordertable,orderid);
        }else if (lastState==2&&newState==3){
            sql = String.format("update %s set orderstate=3 where orderid = %s;", table_ordertable,orderid);
            ifborrowbook=true;
        }else if ((lastState==3&&newState==4)||(lastState==1&&newState==5)||(lastState==2&&newState==5)){
            sql = String.format("update %s set orderstate=%d where orderid = %s;", table_ordertable,newState,orderid);
            ifreturnbook=true;
        }else{
            return false;
        }

        try {
            if (isWork) abstruct_dao.work_begin();
            Statement stat = conn.createStatement();
            stat.execute(sql);
            //借走所有书
            if (ifborrowbook) {
                if (!borrowAll(false)){
                    if (isWork) abstruct_dao.work_rollback();
                    return false;
                }
            }
            //还所有书
            if (ifreturnbook){
                if (!returnAll(false)){
                    if (isWork) abstruct_dao.work_rollback();
                    return false;
                }
            }

            if (isWork) abstruct_dao.work_commit();
            success=true;
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
        }
        return success;
    }

    public static boolean stateChange_noConfirm2comfirm(String orderid){
        orderForm OrderForm = getOrderFormByOrderid(orderid);
        if (OrderForm==null||OrderForm.getOrderstate()!=1) {
            System.err.println("OrderForm may not exist or its state is not 1(no confirm).");
            return false;
        }
        orderForm_dao OrderForm_dao = new orderForm_dao(OrderForm);
        return OrderForm_dao.changeState(2,true);
    }

    public static boolean stateChange_confirm2pay(String orderid){
        orderForm OrderForm = getOrderFormByOrderid(orderid);
        if (OrderForm==null||OrderForm.getOrderstate()!=2) {
            System.err.println("OrderForm may not exist or its state is not 2(comfirm).");
            return false;
        }
        orderForm_dao OrderForm_dao = new orderForm_dao(OrderForm);
        return OrderForm_dao.changeState(3,true);
    }

    public static boolean stateChange_pay2finish(String orderid){
        orderForm OrderForm = getOrderFormByOrderid(orderid);
        if (OrderForm==null||OrderForm.getOrderstate()!=3) {
            System.err.println("OrderForm may not exist or its state is not 3(have pay).");
            return false;
        }
        orderForm_dao OrderForm_dao = new orderForm_dao(OrderForm);
        return OrderForm_dao.changeState(4,true);
    }

    public static boolean stateChange_2failure(String orderid){
        orderForm OrderForm = getOrderFormByOrderid(orderid);
        if (OrderForm==null||OrderForm.getOrderstate()!=1||OrderForm.getOrderstate()!=2) {
            System.err.println("OrderForm may not exist or its state is not 1(no pay) or 2(comfirm).");
            return false;
        }
        orderForm_dao OrderForm_dao = new orderForm_dao(OrderForm);
        return OrderForm_dao.changeState(5,true);
    }
}
