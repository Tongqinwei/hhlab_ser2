package com.util;

/**
 * Created by hasee on 2017/5/9.
 */
public class _math {
    public static String getSerial_number(int no){
        /*
        * 根据数字转为3位序号x
        * */
        if (no<1) {
            System.err.println("The no "+no+" is smaller than 1");
            return "";
        }
        if (no>999) {
            System.err.println("The no "+no+" is bigger than 999");
            return "";
        }
        if (no<10) return "00"+no;
        if (no<100) return "0"+no;
        return ""+no;
    }
    public static String getCaptcha(){
        java.util.Random r=null;
        if (r==null) r=new java.util.Random();
        int ans =r.nextInt(900000)+100000;
        return ""+ans;
    }
}
