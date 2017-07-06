package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String barcodeToIsbn13(String barcode){
        return barcode.substring(0,13);
    }

    public static int culmindiff(String time){
        /*
        * 根据yyyy-MM-dd hh:mm:ss形式时间计算和先在的差距，返回分钟数
        * */
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        format.setLenient(false);
        Date date1 = null;
        try {
            date1 = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = new Date();
        //计算差值，分钟数
        long minutes=(date2.getTime()-date1.getTime())/(1000*60);
        return (int) minutes;
    }

    public static String date2TimeFormat(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(date);
    }
}
