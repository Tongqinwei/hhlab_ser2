package com.dao;

import com.beans.user;

/**
 * Created by lee on 2017/6/26.
 *
 * @author: lee
 * create time: 下午9:30
 */
public class dao_test {
    public static void main(String args[]){
        abstruct_dao.connect();
        user user = user_dao.getUserByUnionId("oycMK0dvrOENuSmjJHXxUs_15Aik");
        System.out.println(user.getUnionid());
    }
}
