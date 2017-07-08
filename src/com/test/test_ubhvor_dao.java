package com.test;

import com.dao.ubhvor_dao;

/**
 * Created by hasee on 2017/7/7.
 */
public class test_ubhvor_dao {
    public static void main(String[] argv){
//        ubhvor_dao.change(1,"1001",1);
//        ubhvor_dao.change(1,"1003",2);
//        ubhvor_dao.change(2,"1002",2);
//        ubhvor_dao.change(3,"1003",4);
//        ubhvor_dao.change(3,"1001",3);
//        ubhvor_dao.flush();
        //ubhvor_dao.bhv_borrow(1,"9787020002207");
        //ubhvor_dao.bhv_comment(2,"9787020002207", (float) 3.0);
        //ubhvor_dao.bhv_getDeatils(1,"9787020024759");
        ubhvor_dao.bhv_order(3,"9787020024759");
        ubhvor_dao.flush();
        String[] re1 = ubhvor_dao.getUserRecommendedIsbn13s(1,1,5);
        for (int i=0;i< re1.length ; i++){
            System.out.println(re1[i]);
        }
    }
}
