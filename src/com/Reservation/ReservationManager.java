package com.Reservation;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lee on 2017/7/5.
 *
 * @author: lee
 * create time: 下午1:18
 */
public class ReservationManager {
    private static ReservationManager ourInstance = new ReservationManager();

    public static ReservationManager getInstance() {
        return ourInstance;
    }

    private ReserveThread thread = null;

    private ReservationManager() {
        this.thread = new ReserveThread();
        this.thread.start();
    }

    public static void update(){
        ReservationManager manager = ReservationManager.getInstance();
        manager.thread.nofifyThread();
    }

    public synchronized static String createOrderID(Date date, String ID, String ISBN){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMDDhhmmsszzz");
        int i = (int)(Math.random()*100);
        return format.format(date)+ID.substring(0,5)+ISBN + Integer.toOctalString(i);
    }
}
