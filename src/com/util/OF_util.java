package com.util;

import com.beans.storage_book;
import com.beans.orderForm;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import sun.rmi.runtime.NewThreadAction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by hasee on 2017/6/26.
 */
public class OF_util {
    private orderForm OrderForm;
    private List<storage_book> temp_books;
    public OF_util(String unionid, String orderid){
        temp_books = new Vector();
        temp_books.clear();
        OrderForm=new orderForm();
        OrderForm.setBook_tot(0);
        OrderForm.setBooks(new storage_book[0]);
        OrderForm.setOrderid(orderid);
        OrderForm.setOrderstate(1);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//可以方便地修改日期格式
        String time = dateFormat.format( now );
        OrderForm.setOrdertime(time);
        OrderForm.setUnionid(unionid);
        OrderForm.setUserid(user_dao.isExistByUnionid(unionid));
    }
    public orderForm toOrderForm(){
        assert temp_books != null;
        OrderForm.setBook_tot(temp_books.size());
        storage_book[] array =new storage_book[temp_books.size()];
        OrderForm.setBooks(temp_books.toArray(array));
        return OrderForm;
    }
    public void add(storage_book sbook){
        temp_books.add(sbook);
    }
    public static String getNewOrderid(){
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");//可以方便地修改日期格式
        String time = dateFormat.format( now );
        String newOrderid=time+_math.getCaptcha();
        while (orderForm_dao.isOrderidExist(newOrderid)){
            newOrderid=time+_math.getCaptcha();
        }
        return newOrderid;
    }
}
