package com.beans;

/**
 * Created by hasee on 2017/5/9.
 */
public class user {
    private int userid;
    private String tel;
    private String unionid;
    private int degree;
    private String birthday;
    private String email;
    private String address;
    private String postcode;
    private String name;
    private int certificate;
    private String certificateid;
    private String image;

    private int recommendFrequency;

    public user(){}
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCertificate() {
        return certificate;
    }

    public void setCertificate(int certificate) {
        this.certificate = certificate;
    }

    public String getCertificateid() {
        return certificateid;
    }

    public void setCertificateid(String certificateid) {
        this.certificateid = certificateid;
    }

    public int getRecommendFrequency() {
        return recommendFrequency;
    }

    public void setRecommendFrequency(int recommendFrequency) {
        this.recommendFrequency = recommendFrequency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public user_brief toBrief(){
        user_brief User_brief = new user_brief();
        User_brief.setCertificate(certificate);
        User_brief.setCertificateid(certificateid);
        User_brief.setEmail(email);
        User_brief.setName(name);
        User_brief.setTel(tel);
        User_brief.setUserid(userid);
        User_brief.setUnionid(unionid);
        return User_brief;
    }

    public user_brief2 toBrief2(){
        user_brief2 User_brief2 = new user_brief2();
        User_brief2.setName(name);
        User_brief2.setName(image);
        return User_brief2;
    }
}
