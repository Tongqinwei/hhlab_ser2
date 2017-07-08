package com.dao;

import com.beans.ubhvor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Created by hasee on 2017/7/5.
 * 操作用户喜好记录
 */
public class ubhvor_dao extends abstruct_dao {
    /*
    * 用于用户行为记录更新
    * */
    public static boolean set(ubhvor Ubhvor){
        long user_id = Ubhvor.getUserid();
        if (user_id<1||Ubhvor.getIsbn13()==null) return false;
        long item_id = Long.parseLong(Ubhvor.getIsbn13());
        float preference = Ubhvor.getGrade();
        int haveBorrow = Ubhvor.getHaveBorrow();
        String sql;
        if (haveBorrow==1||haveBorrow==0){
            if (!isExist(user_id,item_id)) sql = String.format("insert into %s(user_id,item_id,preference,haveBorrow) values (%d,%d,%f,%d);",table_ubhvor,user_id,item_id,preference,haveBorrow);
            else sql = String.format("update %s set preference = %f,haveBorrow = %d where user_id = %d and item_id = %d;",table_ubhvor,preference,haveBorrow,user_id,item_id);
        }else {
            if (!isExist(user_id,item_id)) sql = String.format("insert into %s(user_id,item_id,preference) values (%d,%d,%f);",table_ubhvor,user_id,item_id,preference);
            else sql = String.format("update %s set preference = %f where user_id = %d and item_id = %d;",table_ubhvor,preference,user_id,item_id);
        }

        abstruct_dao.runSQL(sql);
        return true;
    }

    public static boolean set(int userid, String isbn13, float preference,int haveBorrow){
        ubhvor Ubhvor = new ubhvor(userid,isbn13,preference, haveBorrow);
        return set(Ubhvor);
    }

    public static boolean set(int userid, String isbn13, float preference){
        ubhvor Ubhvor = new ubhvor(userid,isbn13,preference, -1);
        return set(Ubhvor);
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

    public static boolean change(int userid, String isbn13, float preference){
        /*
        * 修改喜好值
        * */
        long user_id = userid;
        if (user_id<1||isbn13==null) return false;
        long item_id = Long.parseLong(isbn13);
        String sql;
        if (!isExist(user_id,item_id)) sql = String.format("insert into %s(user_id,item_id,preference) values (%d,%d,%f);",table_ubhvor,user_id,item_id,preference);
        else sql = String.format("update %s set preference = preference + %f where user_id = %d and item_id = %d;",table_ubhvor,preference,user_id,item_id);
        abstruct_dao.runSQL(sql);
        return true;
    }

    public static boolean change(ubhvor Ubhvor){
        return change(Ubhvor.getUserid(),Ubhvor.getIsbn13(),Ubhvor.getGrade());
    }

    public static boolean bhv_borrow(int userid, String isbn13){
        /*
        * 用户借过这本书，喜好设为5,标记借过
        * */
        float preference=5;
        return set(userid,isbn13,preference,0);
    }

    public static boolean bhv_getDeatils(int userid, String isbn13){
        float preference= (float) 0.5;
        return change(userid,isbn13,preference);
    }

    public static boolean bhv_comment(int userid, String isbn13, float grade){
        float preference=0;
        if (grade<2) preference=-1;
        else if (grade<3) preference = (float) 0.5;
        else if (grade<4) preference =1;
        else if (grade<=5.3) preference = (float) 1.5;
        return change(userid,isbn13,preference);
    }

    public static boolean bhv_order(int userid, String isbn13){
        float preference=4;
        return set(userid,isbn13,preference,0);
    }

    public static void flush(){
        /*
        * 刷新最终结果。
        * */
        try {
            culRecommender();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sql = String.format("update %s,%s set %s.haveBorrow=%s.haveBorrow where %s.user_id=%s.user_id and %s.item_id=%s.item_id"
                ,table_ubhvor,table_ubhvor_estimates ,table_ubhvor_estimates ,table_ubhvor,table_ubhvor,table_ubhvor_estimates ,table_ubhvor,table_ubhvor_estimates);
        abstruct_dao.runSQL(sql);
        sql = String.format("update %s set finalWeight = estimates * haveBorrow",table_ubhvor_estimates);
        abstruct_dao.runSQL(sql);
    }

    private static void culRecommender()throws Exception ,TasteException{
        /*
        * 计算推荐值
        * */
        abstruct_dao.connect();
        try {
            Statement stat;
            String sql;
            ResultSet rs;
            ArrayList<Long> userIDs = new ArrayList<Long>();
            ArrayList<Long> bookIDs = new ArrayList<Long>();
            stat = conn.createStatement();

            //构造用户列表
            sql = String.format("select user_id from %s ",table_ubhvor);
            rs = stat.executeQuery(sql);
            while (rs.next()){
                userIDs.add(rs.getLong("user_id"));
            }

            //构造项目列表
            sql = String.format("select item_id from %s ",table_ubhvor);
            rs = stat.executeQuery(sql);
            while (rs.next()){
                bookIDs.add(rs.getLong("item_id"));
            }

            //获取新的分值
            float[][] newpreference=getMat(userIDs,bookIDs);

            //加入新的记录
            for (int i = 0; i < userIDs.size(); i++) {
                for (int j = 0; j < bookIDs.size(); j++) {
                    long user_id=userIDs.get(i);
                    long item_id=bookIDs.get(j);
                    float estimates= newpreference[i][j];
                    //if (!isExist(user_id,item_id)) sql = String.format("insert into %s(user_id,item_id,estimates) values (%d,%d,%f);",table_ubhvor_estimates,user_id,item_id,estimates);
                    //else sql = String.format("update %s set estimates = %f where user_id = %d and item_id = %d;",table_ubhvor_estimates,estimates,user_id,item_id);
                    sql = String.format("replace into %s set estimates = %f ,user_id = %d ,item_id = %d;",table_ubhvor_estimates,estimates,user_id,item_id);
                    abstruct_dao.runSQL(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static float[][] getMat(ArrayList<Long> userIDs, ArrayList<Long> bookIDs) throws Exception ,TasteException{

        if (userIDs.size()==0||bookIDs.size()==0) return null;

        float[][] ans= new float[userIDs.size()][bookIDs.size()];

        String driver = "com.mysql.jdbc.Driver";
        String host = "127.0.0.1";
        String user = "remote_user";
        String password = "remote";
        String databasename = "hhlab";

        Class.forName(driver);
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(databasename);

        JDBCDataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource,
                "ubhvor", "user_id", "item_id", "preference", null);
        ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(jdbcDataModel);

        Recommender recommender = new SVDRecommender(model,
                new ALSWRFactorizer(model, 10, 0.75, 20));

        for (int i = 0; i < userIDs.size(); i++) {
            for (int j = 0; j < bookIDs.size(); j++) {
                ans[i][j] = recommender.estimatePreference(userIDs.get(i), bookIDs.get(j));
            }
        }
        return ans;
    }

    public static String[] getUserRecommendedIsbn13s(int userid,int _begin, int _end){
        String[] ans=null;
        try {
            abstruct_dao.connect();
            Statement stat = conn.createStatement();
            String sql = String.format("select item_id from %s where user_id = %d order by finalWeight desc limit %d,%d"
                    ,table_ubhvor_estimates,userid,_begin-1,_end-1);
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            ans = new String[rs.getRow()];
            int i=0;
            rs.beforeFirst();
            while (rs.next()){
                ans[i++] = ""+rs.getLong("item_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return ans;
        }
    }
}
