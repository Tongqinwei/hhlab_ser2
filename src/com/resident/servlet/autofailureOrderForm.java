package com.resident.servlet;

import com.dao.orderForm_dao;

/**
 * Created by hasee on 2017/7/8.
 */
public class autofailureOrderForm extends Thread {
    public autofailureOrderForm(){}
    @Override
    public void run(){
        System.out.println("autofailureOrderForm create " + this.getId());

        while (true){
            System.out.println("autofailureOrderForm : " + this.getId());

            orderForm_dao.autofailure(20);
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                System.err.println("can not sleep");
                e.printStackTrace();
            }
        }
    }
}
