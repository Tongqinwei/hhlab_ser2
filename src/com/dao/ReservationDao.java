package com.dao;

import com.Reservation.ReservationManager;
import com.beans.ReservationOrder;
import com.util._math;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lee on 2017/7/5.
 *
 * @author: lee
 * create time: 下午1:23
 */
public class ReservationDao extends abstruct_dao {

    /**
     * Mark == 1 : 正在预订
     * Mark == 2 : 预订成功，正在保留
     * Mark == 3 : 约定正常完成
     * Mark == 4 : 约定超时未确认，失效
     * Mark == 5 : 约定异常失效或删除
     */

    private ReservationDao() {
        super();
    }

    public synchronized static boolean inputReservation(String user_id, String ISBN, Date reservationTime) {
        /*
        * 创建一个预订订单，默认状态为 1， 返回错误或正确
        * */
        ReservationDao dao = new ReservationDao();
        abstruct_dao.connect();

        boolean success = false;
        try {
            String sql = String.format("insert into %s(user_id , isbn13 , ordertime, mark, order_id, start_time) values (?,?,?,?,?,?);", table_orderlist);
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, user_id);
            ps.setString(2, ISBN);
            ps.setString(3, _math.date2TimeFormat(reservationTime));
            ps.setString(4, "1");
            ps.setString(5, ReservationManager.createOrderID(reservationTime, user_id, ISBN));
            ps.setString(6, _math.date2TimeFormat(reservationTime));

            ps.execute();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    public static List<ReservationOrder> getOrderByUserID(String UserID) {
        /*
        * 查询用户的所有订单信息，并返回一个list
        * */
        ReservationDao dao = new ReservationDao();
        abstruct_dao.connect();

        List<ReservationOrder> list = new ArrayList<>();
        try {
            String sql = String.format("SELECT * FROM %s WHERE user_id = ?;", table_orderlist);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, UserID);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()) {
                ReservationOrder order = new ReservationOrder();
                order.setISBN(resultSet.getString("isbn13"));
                order.setOrderID(resultSet.getString("order_id"));
                order.setReserveTime(resultSet.getTimestamp("ordertime"));
                order.setStartTime(resultSet.getTimestamp("start_time"));
                order.setState(resultSet.getInt("mark"));
                order.setUser_id(resultSet.getString("user_id"));
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<ReservationOrder> getOrderByISBN(String ISBN) {
        /*
        * 查询用户的所有订单信息，并返回一个list
        * */
        ReservationDao dao = new ReservationDao();
        abstruct_dao.connect();

        List<ReservationOrder> list = new ArrayList<>();
        try {
            String sql = String.format("SELECT * FROM %s WHERE isbn13 = ?;", table_orderlist);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ISBN);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()) {
                ReservationOrder order = new ReservationOrder();
                order.setISBN(resultSet.getString("isbn13"));
                order.setReserveTime(resultSet.getTimestamp("ordertime"));
                order.setStartTime(resultSet.getTimestamp("start_time"));
                order.setState(resultSet.getInt("mark"));
                order.setUser_id(resultSet.getString("user_id"));
                order.setOrderID(resultSet.getString("order_id"));
                order.setBarCode(resultSet.getString("barcode"));
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<ReservationOrder> getRunningStateReservationOrder(){
                /*
        * 查询所有,处于正在预订状态订单信息，并返回一个list
        * */
        ReservationDao dao = new ReservationDao();
        abstruct_dao.connect();

        List<ReservationOrder> list = new ArrayList<>();
        try {
            String sql = String.format("SELECT * FROM %s WHERE mark < 3;", table_orderlist);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()) {
                ReservationOrder order = new ReservationOrder();
                order.setISBN(resultSet.getString("isbn13"));
                order.setState(resultSet.getInt("mark"));
                order.setReserveTime(resultSet.getTimestamp("ordertime"));
                order.setStartTime(resultSet.getTimestamp("start_time"));
                order.setBarCode(resultSet.getString("barcode"));
                order.setUser_id(resultSet.getString("user_id"));
                order.setOrderID(resultSet.getString("order_id"));
                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public synchronized static void updateOrder(ReservationOrder order) throws Exception {
        /*
        * 查询用户的所有订单信息，并返回一个list
        * */
        ReservationDao dao = new ReservationDao();
        abstruct_dao.connect();

        try {
            String sql = String.format("UPDATE %s SET user_id = ?, isbn13 = ?, ordertime = ?, mark = ?, start_time = ?, barcode = ? WHERE order_id = ?", table_orderlist);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, order.getUser_id());
            ps.setString(2, order.getISBN());
            ps.setString(3, _math.date2TimeFormat(order.getReserveTime()));
            ps.setInt(4, order.getState());
            ps.setString(5, _math.date2TimeFormat(order.getStartTime()));
            ps.setString(6,order.getBarCode());
            ps.setString(7, order.getOrderID());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("exception when update order : " + order.getUser_id());
        }
    }




}
