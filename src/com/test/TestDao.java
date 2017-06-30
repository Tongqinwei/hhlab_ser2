package com.test;

import com.beans.adminUser;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.admin_dao;
import com.dao.user_dao;

/**
 * Created by lee on 2017/6/30.
 *
 * @author: lee
 * create time: 上午10:39
 */
public class TestDao {
    public static void main(String args[])
    {
        abstruct_dao.connect();

        user user = new user();
        adminUser user1 = admin_dao.getAdminByLogName("244liyu");

        if(user1.isPasswordMatch("1234")){
            System.out.println("hahha");
        }

        abstruct_dao.close();
    }
}
