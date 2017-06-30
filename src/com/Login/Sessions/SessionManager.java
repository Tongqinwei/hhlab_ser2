package com.Login.Sessions;



import com.Login.Bean.SessionUser;
import com.resident.orderFormFilure;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/6/17.
 *
 * @author: lee
 * create time: 下午2:33
 */
public class SessionManager {

    private Map<String,SessionUser> map;

    private SecureRandom random;

    private SessionTimeOutChecker checker;

    private static SessionManager ourInstance = new SessionManager();

    public static SessionManager getInstance() {
        return ourInstance;
    }

    private SessionManager() {
        // 隐藏 构造方法，实现单例模式的代码
        map = new HashMap<>();
        random = new SecureRandom();
        checker = new SessionTimeOutChecker(map);
        checker.start();
    }

    private synchronized String GenerateSessionID(){
        // 生成唯一的key
        String key = new BigInteger(130, random).toString(32);
        while (map.containsKey(key)){
            key = new BigInteger(130, random).toString(32);
        }
        return key;
    }

    public SessionUser getUser(String sessionID){
//      通过获取的session ID 来查询对象是否存在，如果不存在则说明未登录
        SessionUser user = map.get(sessionID);
        if(user != null){
            user.Update();
        }
        return user;
    }

    public boolean removeUser(String ID){
//        通过ID 删除一个用户
        return null != map.remove(ID);
    }

    public void AddUser(SessionUser user){
//       添加用户到session 中
        user.setSessionID(GenerateSessionID());
        user.Update();
        map.put(user.getSessionID(), user);
    }
}
