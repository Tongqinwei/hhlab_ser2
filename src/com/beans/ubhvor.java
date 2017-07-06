package com.beans;

import com.dao.user_dao;

/**
 * Created by hasee on 2017/7/5.
 */
public class ubhvor {
    private int userid;
    private String unionid;
    private String isbn13;
    private double grade;
    public ubhvor(){}
    public ubhvor(int userid,String isbn13,double grade){
        this.userid=userid;
        this.isbn13=isbn13;
        this.grade=grade;
        user User = user_dao.getUserByUserid(userid);
        if (User!=null) this.unionid=User.getUnionid();
    }
    public ubhvor(String unionid,String isbn13,double grade){
        this.unionid=unionid;
        this.isbn13=isbn13;
        this.grade=grade;
        user User = user_dao.getUserByUnionId(unionid);
        if (User!=null) this.userid = User.getUserid();
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
