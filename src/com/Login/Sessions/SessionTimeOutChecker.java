package com.Login.Sessions;



import com.Login.Bean.SessionUser;
import com.Reservation.ReservationManager;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lee on 2017/6/17.
 * 用于便利Map 检查当前的所有用户是否已经过期，设置过期时间
 * @author: lee
 * create time: 下午4:38
 */
public class SessionTimeOutChecker extends Thread {
    private Map map = null;

    // 超时时间默认为 20分钟
    public static Long OutTime = 1200000L;


    SessionTimeOutChecker(Map map){
        this.map = map;
    }

    @Override
    public synchronized void start() {
        super.start();

    }

    @Override
    public void run() {
        Map.Entry<String, SessionUser> entry ;
        while(true){

            Date now = new Date();
            Iterator<Map.Entry<String, SessionUser>> it = map.entrySet().iterator();
            while(it.hasNext()){
                // 遍历整个Map 如果出现已经超过20分钟没有update的对象就删除

                entry = it.next();

                if(now.getTime() - entry.getValue().LastUpdate.getTime()  > OutTime){
                    it.remove();
                }
            }

            // 启动线程，更新预订信息
            ReservationManager.update();

            // 默认一分钟检查一次超时的对象
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
