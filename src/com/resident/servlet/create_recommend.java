package com.resident.servlet;

import com.dao.abstruct_dao;
import com.dao.ubhvor_dao;

/**
 * Created by hasee on 2017/7/8.
 */
public class create_recommend extends Thread {
    public create_recommend(){}
    @Override
    public void run(){
        System.out.println("create_recommend create " + this.getId());

        while (true){
            System.out.println("create_recommend : " + this.getId());

            abstruct_dao.connect();
            ubhvor_dao.flush();
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("can not sleep");
                e.printStackTrace();
            }
            abstruct_dao.close();
        }
    }
}
