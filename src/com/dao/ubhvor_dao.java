package com.dao;

import com.beans.ubhvor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by hasee on 2017/7/5.
 */
public class ubhvor_dao extends abstruct_dao {
    /*
    * 用于用户行为记录更新
    * */
    public static boolean add(ubhvor Ubhvor){
        long user_id = Ubhvor.getUserid();
        if (user_id<1||Ubhvor.getIsbn13()==null) return false;
        long item_id = Long.parseLong(Ubhvor.getIsbn13());
        double preference = Ubhvor.getGrade();
        String sql;
        if (!isExist(user_id,item_id)) sql = String.format("insert into %s(user_id,item_id,preference) values (%d,%d,%f);",table_ubhvor,user_id,item_id,preference);
        else sql = String.format("update %s set preference = %f where user_id = %d and item_id = %d;",table_ubhvor,preference,user_id,item_id);
        abstruct_dao.runSQL(sql);
        return true;
    }

    public static boolean add(int userid, String isbn13, double preference){
        ubhvor Ubhvor = new ubhvor(userid,isbn13,preference);
        return add(Ubhvor);
    }

    public static boolean isExist(long user_id ,long item_id){
        /*
        * 某一用户行为是否存在
        * */
        abstruct_dao.connect();
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where user_id = %d and item_id = %d;", table_ubhvor,user_id,item_id);
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
