package com.beans;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午6:54
 */
public class adminUser {
    private String loginName;
    private String password;
    private String bindOpenID;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBindOpenID() {
        return bindOpenID;
    }

    public void setBindOpenID(String bindOpenID) {
        this.bindOpenID = bindOpenID;
    }

    private static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isPasswordMatch(String inputPassword) {
        if( this.password != null){
            try {
                return this.password.contentEquals(getMD5(inputPassword));
            } catch (NullPointerException e){
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
