package com.util;

/**
 * Created by hasee on 2017/5/18.
 */
public class authorutil {
    public static String authorarr2String(String[] author){
        String ans=author[0];
        for (int i=1;i<author.length;i++){
            ans+=","+author[i];
        }
        return ans;
    }
    public static String[] string2Authorarr(String s){
        String[] ans=s.split(",");
        return ans;
    }
}
