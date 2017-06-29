package com.dao;

import com.beans.adminUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午6:53
 */
public class admin_dao extends abstruct_dao {
    private admin_dao() {
        super();
    }

    public static boolean addUser(adminUser user){
        admin_dao dao = new admin_dao();
        abstruct_dao.connect();

        boolean success=false;
        try {
            String sql = String.format("insert into %s(login_name , password , user_id)values (?,MD5(?),?);", table_admin);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getLoginName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getBindOpenID());
            ps.execute();
            success =  true;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
//            throw new RuntimeException(e);
        }finally {
            abstruct_dao.close();
        }
        return success;
    }



    public static adminUser getAdminByOpenID(String Openid){
        admin_dao dao = new admin_dao();
        abstruct_dao.connect();

        adminUser user = null;
        try {
            String sql = String.format("SELECT * FROM %s WHERE user_id = ?;", table_admin);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Openid);
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            while (resultSet.next()){
                user = new adminUser();
                user.setBindOpenID(resultSet.getString("user_id"));
                user.setLoginName(resultSet.getString("login_name"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }finally {
            abstruct_dao.close();
        }
        return user;
    }

    public static adminUser getAdminByLogName(String LogName){
        admin_dao dao = new admin_dao();
        abstruct_dao.connect();

        adminUser user = null;
        try {
            String sql = String.format("SELECT * FROM %s WHERE login_name = ?;", table_admin);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, LogName);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                user = new adminUser();
                user.setBindOpenID(resultSet.getString("user_id"));
                user.setLoginName(resultSet.getString("login_name"));
                user.setPassword(resultSet.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            user = null;
        }finally {
            abstruct_dao.close();
        }
        return user;
    }

}
