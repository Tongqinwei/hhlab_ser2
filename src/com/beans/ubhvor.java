package com.beans;

import com.dao.user_dao;

/**
 * Created by hasee on 2017/7/5.
 */
public class ubhvor {
    private int userid;
    private String unionid;
    private String isbn13;
    private float grade;
    private int haveBorrow;
    private float finalWeight;
    private float estimates;
    public ubhvor(){}
    public ubhvor(int userid,String isbn13,float grade,int haveBorrow){
        this.userid=userid;
        this.isbn13=isbn13;
        this.grade=grade;
        //user User = user_dao.getUserByUserid(userid);
        //if (User!=null) this.unionid=User.getUnionid();
        this.haveBorrow=haveBorrow;
    }
    public ubhvor(String unionid,String isbn13,float grade,int haveBorrow){
        this.unionid=unionid;
        this.isbn13=isbn13;
        this.grade=grade;
        //user User = user_dao.getUserByUnionId(unionid);
        //if (User!=null) this.userid = User.getUserid();
        this.haveBorrow=haveBorrow;
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

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getHaveBorrow() {
        return haveBorrow;
    }

    public void setHaveBorrow(int haveBorrow) {
        this.haveBorrow = haveBorrow;
    }

    public float getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(float finalWeight) {
        this.finalWeight = finalWeight;
    }

    public float getEstimates() {
        return estimates;
    }

    public void setEstimates(float estimates) {
        this.estimates = estimates;
    }
}
