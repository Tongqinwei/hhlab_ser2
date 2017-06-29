package com.resident;

import com.dao.orderForm_dao;

/**
 * Created by hasee on 2017/6/30.
 */
public class orderFormFilure extends Thread{
    private static orderFormFilure ourInstance = new orderFormFilure();

    public static orderFormFilure getInstance() {
        return ourInstance;
    }

    private orderFormFilure() {
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
//        super.run();
        while (true){
            System.out.println(0);
            orderForm_dao.autofailure(2);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
