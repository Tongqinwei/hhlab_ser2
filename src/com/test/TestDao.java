package com.test;


import com.Reservation.ReservationTool;
import com.beans.ReservationOrder;
import com.dao.ReservationDao;
import com.google.gson.JsonArray;

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
        JsonArray array = new JsonArray();
        array.add("adasd");
        array.add("asdasd");
        System.out.println(array.toString());
        System.out.println(ReservationTool.returnAsJson(true,"aa","su",array));
    }
}
