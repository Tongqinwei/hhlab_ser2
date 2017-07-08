package com.Login.Handler;

import ssm.tools.HttpConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/6/17.
 *
 * @author: lee
 * create time: 下午3:24
 */
public class WechatSessionHandler {

//    https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code

    private final static String AppID = "wxa91dffb4b955bb2f";
    private final static String secret = "2bea0ecf3d80ae58b27185fd9904ebc0";
    private final static String grant_type = "authorization_code";
    private final static String TargetUrl = "https://api.weixin.qq.com/sns/jscode2session";

/**
 * 请求微信的URL 获取登录的session数据
 * */

    public static String getSessionKey(String sessionCode){

        Map<String, String> map = new HashMap<>();
        map.put("appid",AppID);
        map.put("secret",secret);
        map.put("js_code",sessionCode);
        map.put("grant_type",grant_type);

        return HttpConnector.PostSSL(TargetUrl,map);
    }



}

