package com.resident.servlet;

/**
 * Created by hasee on 2017/7/8.
 */
public class create_recommend extends Thread {
    public create_recommend(){}
    @Override
    public void run(){
        while (true){

            try {
                sleep(24*60*1000);
            } catch (InterruptedException e) {
                System.err.println("can not sleep");
                e.printStackTrace();
            }
        }
    }
}
