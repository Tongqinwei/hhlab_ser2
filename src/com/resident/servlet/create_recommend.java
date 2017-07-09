package com.resident.servlet;

import com.dao.ubhvor_dao;

/**
 * Created by hasee on 2017/7/8.
 */
public class create_recommend extends Thread {
    public create_recommend(){}
    @Override
    public void run(){
        while (true){
            ubhvor_dao.flush();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("can not sleep");
                e.printStackTrace();
            }
        }
    }
}
