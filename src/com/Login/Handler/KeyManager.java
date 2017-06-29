package com.Login.Handler;

import java.util.Properties;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午10:16
 */
public class KeyManager extends Thread{
    private static KeyManager ourInstance = new KeyManager();

    public static KeyManager getInstance() {
        return ourInstance;
    }

    private rsaManager rsaManager;

    private KeyManager() {
        rsaManager = new rsaManager();
        rsaManager.updateKey();

        this.start();
    }

    public Properties getKey(){
        return this.rsaManager.getKey();
    }

    public com.Login.Handler.rsaManager getRsaManager() {
        return rsaManager;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                sleep(1200000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rsaManager.updateKey();
        }
    }
}
