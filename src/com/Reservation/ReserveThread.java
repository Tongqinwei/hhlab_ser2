package com.Reservation;

import com.beans.ReservationOrder;
import com.beans.storage_book;
import com.beans.user;
import com.dao.ReservationDao;
import com.dao.storage_book_dao;
import com.dao.user_dao;

import java.util.Date;
import java.util.List;

/**
 * Created by lee on 2017/7/5.
 *
 * @author: lee
 * create time: 下午1:22
 */
class ReserveThread extends Thread{

    private List<ReservationOrder> list = null;

    private void update(){
        System.out.println("updated!");
        if(list != null){
            list.clear();
        }
        list = ReservationDao.getRunningStateReservationOrder();
        ReservationTool.listSortToArray(list);
    }


    @Override
    public synchronized void start() {
        System.out.println("thread started");
        super.start();
    }

    @Override
    public void run() {
        while (true){
            update();

            for(ReservationOrder order : this.list){
                System.out.println("check order " + order.getOrderID());
                if(order.getState() == 1){
                    // 订单是正在预约的状态下
                    // 尝试预订图书

                    storage_book storageBook = ReservationTool.reserveBookIfAvailable(order.getISBN());
                    if(storageBook != null){
                        // 预订成功！设置为正在保留的状态,将失效时间设为3天后
                        order.setState(2);
                        order.setStartTime(ReservationTool.get3DaysLater());
                        order.setBarCode(storageBook.getBook_id());

                        System.out.println("book success! barcode : " + storageBook.getBook_id());

                        try {
                            ReservationDao.updateOrder(order);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("send message!");
                        // 发送用户短信
                        user dbUser = user_dao.getUserByUnionId(order.getUser_id());
                        ReservationTool.sendBookSuccess(order,dbUser.getTel());
                    }
                    // 预订失败就继续序列
                } else if (order.getState() == 2){
                    // 如果订单是正在进行的，
                    // 检查是否已经超时
                    Date now = new Date();
                    if(order.getStartTime().getTime() - now.getTime() <= 0){
                        // 已经超时了，就修改状态

                        // 更新图书状态，将其改为空闲
                        storage_book_dao.updateState(order.getBarCode(),"available");

                        System.out.println("reservation order : " + order.getOrderID() + " out of time");
                        // 更新订单状态
                        order.setState(4);
                        try {
                            ReservationDao.updateOrder(order);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // 发送用户失效短信
                        user dbUser = user_dao.getUserByUnionId(order.getUser_id());
                        ReservationTool.sendBookOutOfTime(order,dbUser.getTel());

                    }
                }
            }

            //线程等待被唤醒
            this.waitThread();
        }
    }

    private synchronized void waitThread(){
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void nofifyThread(){
        this.notify();
    }
}
