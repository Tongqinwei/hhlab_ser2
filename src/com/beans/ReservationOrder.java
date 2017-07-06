package com.beans;

import java.util.Date;

/**
 * Created by lee on 2017/7/5.
 *
 * @author: lee
 * create time: 下午1:57
 */
public class ReservationOrder{

    /**
     * Mark == 1 : 正在预订
     * Mark == 2 : 预订成功，正在保留
     * Mark == 3 : 约定正常完成
     * Mark == 4 : 约定超时未确认，失效
     * Mark == 5 : 约定异常失效或删除
     */


    private String ISBN;
    private String user_id;
    private int state;
    private Date reserveTime;
    private Date startTime;
    private String OrderID;
    private String BarCode;

    public String getBarCode() {
        return BarCode;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getReserveTime() {
        return reserveTime;
    }

    public void setReserveTime(Date reserveTime) {
        this.reserveTime = reserveTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
