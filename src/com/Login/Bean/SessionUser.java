package com.Login.Bean;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/6/17.
 *
 * @author: lee
 * create time: 下午3:46
 */
public class SessionUser {
//    用户的公开微信号
    private String OpenID;
//    用户本次登录设置的sessionID
    private String SessionID;
//    用户的会话秘钥
    private String SessionKey;
//    用户的权限设置
    private int power;

//    用户的登录手机号，用户检查用户是否已经登录
    private String CellPhone;

    public Date LastUpdate;

    public Map<String,Object> ObjectMap;

    public SessionUser(){
        OpenID = null;
//    用户本次登录设置的sessionID
        SessionID = null;
//    用户的会话秘钥
        SessionKey = null;
//    用户的权限设置
        power = 0;

//    用户的登录手机号，用户检查用户是否已经登录
        CellPhone = null;

        ObjectMap = new HashMap<>();

        LastUpdate = new Date();
    }

    public void Update(){
        this.LastUpdate = new Date();
    }

    public boolean isUserLogged(){
//      返回用户是否登录的状态
        return !(OpenID == null && CellPhone == null);
    }

    public boolean isLoggedByWechat(){
//      查询用户是否通过微信登录
        return OpenID != null;
    }

    public boolean isLoggedByPhone(){
//      查询用户是否通过手机登录
        return CellPhone != null;
    }


    public String getOpenID() {
        return OpenID;
    }

    public void setOpenID(String openID) {
        OpenID = openID;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getSessionKey() {
        return SessionKey;
    }

    public void setSessionKey(String sessionKey) {
        SessionKey = sessionKey;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }


    public String getCellPhone() {
        return CellPhone;
    }

    public void setCellPhone(String cellPhone) {
        CellPhone = cellPhone;
    }
}
