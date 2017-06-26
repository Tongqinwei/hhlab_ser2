package com.test;

import com.beans.orderForm;
import com.beans.storage_book;
import com.dao.orderForm_dao;
import com.util.OF_util;

/**
 * Created by hasee on 2017/6/26.
 */
public class testOrderForm {
    public static void main(String argc[]){
        String orderid=OF_util.getNewOrderid();
        OF_util of_util =  new OF_util("novas_ghost",orderid);
        storage_book book1 = new storage_book();
        book1.setBook_id("9787020049295001");
        of_util.add(book1);
        storage_book book2 = new storage_book();
        book2.setBook_id("9787208061644001");
        of_util.add(book2);
        orderForm OrderForm = of_util.toOrderForm();
        orderForm_dao OFD = new orderForm_dao(OrderForm);
        System.out.println(OFD.add());
        OFD.borrowAll();
    }
}
