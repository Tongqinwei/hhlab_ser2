package com.dao;

import com.beans.ubhvor;

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
        String sql_insert="insert into %s(user_id,item_id,preference) values (%ld,%ld,%lf);";
        String sql;
        sql = String.format(sql_insert,table_ubhvor,user_id,item_id,preference);
        abstruct_dao.runSQL(sql);
        return true;
    }

    public static boolean add(int userid, String isbn13, double preference){
        ubhvor Ubhvor = new ubhvor(userid,isbn13,preference);
        return add(Ubhvor);
    }
}
