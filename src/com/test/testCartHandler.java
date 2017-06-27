package com.test;

import com.beans.book_brief;
import com.dao.cart_dao;
import net.sf.json.JSONArray;

/**
 * Created by hasee on 2017/6/27.
 */
public class testCartHandler {
    public static void main(String argc[]){
        book_brief[] books= cart_dao.getbooks(1);
        JSONArray book_brief_json = JSONArray.fromObject(books);
        System.out.println(book_brief_json.toString());
    }
}
