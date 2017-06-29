package com.test;

import com.beans.book_brw;
import com.beans.orderForm;
import com.beans.storage_book;
import com.dao.orderForm_dao;
import com.util.OF_util;
import net.sf.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hasee on 2017/6/26.
 */
public class testOrderForm {
    public static void main(String argc[]){
        String orderid=OF_util.getNewOrderid();
        OF_util of_util =  new OF_util("novas_ghost",orderid);
        book_brw book1 = new book_brw();
        book1.setBarcode("9787020049295001");
        of_util.add(book1);
        book_brw book2 = new book_brw();
        book2.setBarcode("9787208061644001");
        of_util.add(book2);
        orderForm OrderForm = of_util.toOrderForm();
        orderForm_dao OFD = new orderForm_dao(OrderForm);
        System.out.println(OFD.add(true));
//        orderForm[] OrderForms = orderForm_dao.getOrderFormsByunionid("novas_ghost");
//        JSONArray book_brief_json = JSONArray.fromObject(OrderForms);
//        System.out.println( book_brief_json.toString());
//        Date now = new Date();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = dateFormat.format( now );
//        System.out.println( time);
//        try {
//            Date now2 = dateFormat.parse(time);
//            time = dateFormat.format( now );
//            System.out.println( time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}
