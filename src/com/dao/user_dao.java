package com.dao;
import com.beans.*;
import com.JDBC.*;
import com.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.List;
import java.util.Vector;
/**
 * Created by hasee on 2017/5/9.
 */
public class user_dao extends abstruct_dao{
    private user User;

    public user_dao(user User){
        super();
        this.User=User;
    }

    public user_dao(Connection conn,user User){
        super(conn);
        this.User=User;
    }

    public boolean add_user(){
        /*user
        * 加入新用户
        * */
        boolean success=false;
        try {
            String sql = String.format("insert into %s(tel , unionid , degree , birthday , email , address , postcode ,name , certificate , certificateid , recommendFrequency)values (?,?,?,?,?,?,?,?,?,?,?);", table_user);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, User.getTel());
            ps.setString(2, User.getUnionid());
            ps.setInt(3, User.getDegree());
            ps.setString(4, User.getBirthday());
            ps.setString(5, User.getEmail());
            ps.setString(6, User.getAddress());
            ps.setString(7, User.getPostcode());
            ps.setString(8, User.getName());
            ps.setInt(9, User.getCertificate());
            ps.setString(10,User.getCertificateid());
            ps.setInt(11,User.getRecommendFrequency());
            ps.execute();
            success =  true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean update_user_byTel(){
        /*user
        * 利用电话更新user
        * */
        boolean success=false;
        try {
            String sql = String.format("update %s set degree = ? , birthday = ? , email = ? , address = ? , postcode = ?  ,name = ? , certificate = ? , certificateid = ? , recommendFrequency = ? where tel = ? ;", table_user);        //9
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, User.getDegree());
            ps.setString(2, User.getBirthday());
            ps.setString(3, User.getEmail());
            ps.setString(4, User.getAddress());
            ps.setString(5, User.getPostcode());
            ps.setString(6, User.getName());
            ps.setInt(7, User.getCertificate());
            ps.setString(8,User.getCertificateid());
            ps.setInt(9,User.getRecommendFrequency());
            ps.setString(10,User.getTel());
            ps.execute();
            success=true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean update_user_byUnionID(){
        /*user
        * 利用unionid更新user
        * */
        boolean success=false;
        try {
            String sql = String.format("update %s set degree = ? , birthday = ? , email = ? , address = ? , postcode = ?  ,name = ? , certificate = ? , certificateid = ?  ,tel=?,recommendFrequency= ? where unionid = ? ;", table_user);        //9
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, User.getDegree());
            ps.setString(2, User.getBirthday());
            ps.setString(3, User.getEmail());
            ps.setString(4, User.getAddress());
            ps.setString(5, User.getPostcode());
            ps.setString(6, User.getName());
            ps.setInt(7, User.getCertificate());
            ps.setString(8,User.getCertificateid());
            ps.setString(9,User.getTel());
            ps.setInt(10,User.getRecommendFrequency());
            ps.setString(11,User.getUnionid());
            ps.execute();
            success= true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public boolean isExistByUserid(){
        try {
            Statement stat = conn.createStatement();
            String sql = "select * from "+table_user+" where userid="+User.getUserid()+";";
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static boolean isExistByUserid(int userid){
        user User = new user();
        User.setUserid(userid);
        user_dao User_dao = new user_dao(User);
        return User_dao.isExistByUserid();
    }

    public int isExistByUnionid(){
        /*
        * 返回userid 失败返回-1
        * */
        if (User.getUnionid()==null||User.getUnionid()=="") return -1;
        try {
            Statement stat = conn.createStatement();
            String sql = "select userid from "+table_user+" where unionid=\'"+User.getUnionid()+"\';";
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return -1;
            else return rs.getInt("userid");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int isExistByUnionid(String unionid){
        user User = new user();
        User.setUnionid(unionid);
        user_dao User_dao = new user_dao(User);
        return User_dao.isExistByUnionid();
    }

    public int isExistByTel(){
        /*
        * 返回userid 失败返回-1
        * */
        if (User.getTel()==null||User.getTel()=="") return -1;
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select userid from %s where tel='%s';", table_user, User.getTel());
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return -1;
            else return rs.getInt("userid");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static int isExistByTel(String tel){
        user User = new user();
        User.setTel(tel);
        user_dao User_dao = new user_dao(User);
        return User_dao.isExistByTel();
    }

    public boolean isExist(){
        /*
        * by unionid or userid or tel than exist
        * */
        return this.isExistByUnionid()>=0 || this.isExistByUserid() || this.isExistByTel()>=0;
    }

    public static boolean add(user User){
        user_dao User_dao = new user_dao(User);
        return User_dao.add_user();
    }

    public String getTrueNameByUserid(){
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where userid=%d;", table_user, User.getUserid());
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return null;
            else return rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String getTrueNameByUserid(int userid)throws NullPointerException{
        user User = new user();
        User.setUserid(userid);
        user_dao User_dao = new user_dao(User);
        return User_dao.getTrueNameByUserid();
    }

    public static user getUserByUnionId(String unionId)throws NullPointerException{
        /*
        * 通过unionid获取
        * */
        abstruct_dao.connect();
        if (unionId==null||unionId=="") return new user();
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where unionid='%s';", table_user, unionId);
            ResultSet rs = stat.executeQuery(sql);
//
            user User = null;
            while(rs.next()){
                User = new user();
                User.setUserid(rs.getInt("userid"));
                User.setTel(rs.getString("tel"));
                User.setUnionid(rs.getString("unionid"));
                User.setDegree(rs.getInt("degree"));
                User.setBirthday(rs.getString("birthday"));
                User.setEmail(rs.getString("email"));
                User.setAddress(rs.getString("address"));
                User.setPostcode(rs.getString("postcode"));
                User.setName(rs.getString("name"));
                User.setCertificate(rs.getInt("certificate"));
                User.setCertificateid(rs.getString("certificateid"));
                User.setRecommendFrequency(rs.getInt("recommendFrequency"));
                User.setImage(rs.getString("image"));
                User.setFollow(rs.getInt("follow"));
                User.setFan(rs.getInt("fan"));
            }
            return User;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean update_user_byUnionID(user User)throws NullPointerException{
        user_dao User_dao = new user_dao(User);
        return User_dao.update_user_byUnionID();
    }

    public static user getUserByUserid(int userid)throws NullPointerException{
        abstruct_dao.connect();
        if (userid<=0) return null;
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where userid=%s;", table_user, userid);
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            user User = new user();
            User.setUserid(rs.getInt("userid"));
            User.setTel(rs.getString("tel"));
            User.setUnionid(rs.getString("unionid"));
            User.setDegree(rs.getInt("degree"));
            User.setBirthday(rs.getString("birthday"));
            User.setEmail(rs.getString("email"));
            User.setAddress(rs.getString("address"));
            User.setPostcode(rs.getString("postcode"));
            User.setName(rs.getString("name"));
            User.setCertificate(rs.getInt("certificate"));
            User.setCertificateid(rs.getString("certificateid"));
            User.setRecommendFrequency(rs.getInt("recommendFrequency"));
            User.setImage(rs.getString("image"));
            User.setFollow(rs.getInt("follow"));
            User.setFan(rs.getInt("fan"));
            return User;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static user[] getAllUsers(int _begin,int _end)throws NullPointerException{
        abstruct_dao.connect();
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s limit %d,%d;", table_user, _begin-1,_end-1);
            ResultSet rs = stat.executeQuery(sql);
            List<user> users=new Vector<user>();
            users.clear();
            while (rs.next()){
                user User = new user();
                User.setUserid(rs.getInt("userid"));
                User.setTel(rs.getString("tel"));
                User.setUnionid(rs.getString("unionid"));
                User.setDegree(rs.getInt("degree"));
                User.setBirthday(rs.getString("birthday"));
                User.setEmail(rs.getString("email"));
                User.setAddress(rs.getString("address"));
                User.setPostcode(rs.getString("postcode"));
                User.setName(rs.getString("name"));
                User.setCertificate(rs.getInt("certificate"));
                User.setCertificateid(rs.getString("certificateid"));
                User.setRecommendFrequency(rs.getInt("recommendFrequency"));
                User.setImage(rs.getString("image"));
                User.setFollow(rs.getInt("follow"));
                User.setFan(rs.getInt("fan"));
                users.add(User);
            }
            user[] array =new user[users.size()];
            return users.toArray(array);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static user_brief[] getAllUser_bf(int _begin,int _end)throws NullPointerException{
        user[] users=getAllUsers(_begin,_end);
        user_brief[] user_briefs= new user_brief[users.length];
        for (int i=0;i<users.length;i++){
            user_briefs[i]=users[i].toBrief();
        }
        return user_briefs;
    }

    public static user[] searchUser(String key,int _begin,int _end){
        /*
        * by name,email,tel
        * */
        String sql = String.format("select * from  %s where email = ? or name like ? or tel = ? limit ?,? ;", table_user);
        PreparedStatement ps = null;
        try {
            abstruct_dao.connect();
            ps = conn.prepareStatement(sql);
            ps.setString(1, key);
            ps.setString(2, "%"+key+"%");
            ps.setString(3, key);
            ps.setInt(4, _begin-1);
            ps.setInt(5, _end-1);
            ResultSet rs = ps.executeQuery();
            List<user> users=new Vector<user>();
            users.clear();
            while (rs.next()){
                user User = new user();
                User.setUserid(rs.getInt("userid"));
                User.setUnionid(rs.getString("unionid"));
                User.setDegree(rs.getInt("degree"));
                User.setBirthday(rs.getString("birthday"));
                User.setEmail(rs.getString("email"));
                User.setTel(rs.getString("tel"));
                User.setAddress(rs.getString("address"));
                User.setPostcode(rs.getString("postcode"));
                User.setName(rs.getString("name"));
                User.setCertificate(rs.getInt("certificate"));
                User.setCertificateid(rs.getString("certificateid"));
                User.setRecommendFrequency(rs.getInt("recommendFrequency"));
                User.setImage(rs.getString("image"));
                User.setFollow(rs.getInt("follow"));
                User.setFan(rs.getInt("fan"));
                users.add(User);
            }
            user[] array =new user[users.size()];
            return users.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
