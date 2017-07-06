package com.test;


import com.beans.ReservationOrder;
import com.dao.ReservationDao;

import java.util.Date;
import java.util.List;

/**
 * Created by lee on 2017/6/30.
 *
 * @author: lee
 * create time: 上午10:39
 */
public class TestDao {
    public static void main(String args[])
    {
        ReservationOrder order = null;
        if(ReservationDao.inputReservation("oycMK0dvrOENuSmjJHXxUs_15Aik","9780316346627",new Date())){
            List<ReservationOrder> orderList = ReservationDao.getOrderByUserID("oycMK0dvrOENuSmjJHXxUs_15Aik");
            order = orderList.get(0);
        }
        assert order != null;
        order.setState(5);
        try {
            ReservationDao.updateOrder(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
