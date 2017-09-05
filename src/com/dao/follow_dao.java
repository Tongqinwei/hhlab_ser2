package com.dao;

import com.beans.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

/**
 * Created by hasee on 2017/9/2.
 */
public class follow_dao extends abstruct_dao {

    public static void add(int userid1,int userid2){
        String sql=String.format("INSERT INTO %s (userid1, userid2) VALUES (%d,%d);",table_follow_fan,userid1,userid2);
        abstruct_dao.runSQL(sql);
    }

    public static void del(int userid1,int userid2){
        String sql=String.format("DELETE FROM %s WHERE userid1=%d AND userid2=%d ;",table_follow_fan,userid1,userid2);
        abstruct_dao.runSQL(sql);
    }

    public static boolean isFollower(int userid1,int userid2){
        String sql=String.format("SELECT * FROM %s WHERE userid1=%d AND userid2=%d ;",table_follow_fan,userid1,userid2);
        try {
            abstruct_dao.connect();
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            abstruct_dao.close();
            return rs.getRow() != 0;
        } catch (SQLException e) {
            abstruct_dao.close();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void add(String unionid1,String unionid2){
        int userid1=user_dao.isExistByUnionid(unionid1);
        if (userid1<=0) return ;
        int userid2=user_dao.isExistByUnionid(unionid2);
        if (userid2<=0) return ;
        add(userid1,userid2);
    }

    public static void del(String unionid1,String unionid2){
        int userid1=user_dao.isExistByUnionid(unionid1);
        if (userid1<=0) return ;
        int userid2=user_dao.isExistByUnionid(unionid2);
        if (userid2<=0) return ;
        del(userid1,userid2);
    }

    public static boolean isFollow(String unionid1,String unionid2) {
        int userid1 = user_dao.isExistByUnionid(unionid1);
        if (userid1 <= 0) return false;
        int userid2 = user_dao.isExistByUnionid(unionid2);
        return userid2 > 0 && isFollower(userid1, userid2);
    }

    public static user[] getFollowsOrFans(int userid,String mode){
        String sql;
        String getstring;
        switch (mode) {
            case "follows":
            case "Follows":
            case "follow":
            case "Follow":
                sql = String.format("SELECT * FROM %s WHERE userid1=%d ;", table_follow_fan, userid);
                getstring="userid2";
                break;
            case "fans":
            case "Fans":
            case "fan":
            case "Fan":
                sql = String.format("SELECT * FROM %s WHERE userid2=%d ;", table_follow_fan, userid);
                getstring="userid1";
                break;
            default:
                return null;
        }
        try {
            abstruct_dao.connect();
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            List<user> Users= new Vector();
            while (rs.next()){
                Users.add(user_dao.getUserByUserid(rs.getInt(getstring)));
            }
            abstruct_dao.close();
            user[] array =new user[Users.size()];
            return Users.toArray(array);
        } catch (SQLException e) {
            abstruct_dao.close();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
