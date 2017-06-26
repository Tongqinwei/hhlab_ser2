package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

/**
 * Created by lee on 2017/6/26.
 *
 * @author: lee
 * create time: 下午6:59
 */
public class UserInfoModificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(404);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        abstruct_dao.connect();
        // 获取登录信息
        JsonObject jsonObject = null;
        String sessionID = null;
        SessionUser sessionUser = null;

        Writer out = resp.getWriter();

        // try to get the json and the session user
        try{
            String UserInfo = CreateSessionServlet.getBody(req);
            log("get user info :" + UserInfo);
            jsonObject = MyJsonParser.String2Json(UserInfo);
            sessionID = jsonObject.get("session_id").getAsString();
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(sessionID);

            if(sessionUser == null){
                throw new Exception("no such a user and return the error info");
            }
        } catch (Exception e){
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error json"));
            out.flush();
            out.close();
            resp.flushBuffer();
            e.printStackTrace();
            abstruct_dao.close();
            return;
        }


        if(sessionUser.isLoggedByWechat()){
            user user = null;
            user = user_dao.getUserByUnionId(sessionUser.getOpenID());

            // 用户是本人
            if(user.getUnionid().contentEquals(sessionUser.getOpenID())){
                // if the user is him self
                try {
                    user.setAddress(jsonObject.get("address").getAsString());
                    user.setDegree(jsonObject.get("degree").getAsInt());
                    user.setEmail(jsonObject.get("email").getAsString());
                    user.setName(jsonObject.get("name").getAsString());
                    user.setCertificate(jsonObject.get("cert").getAsInt());
                    user.setCertificateid(jsonObject.get("cert_id").getAsString());
                    user.setBirthday(jsonObject.get("birthday").getAsString());
                } catch (Exception e){
                    e.printStackTrace();
                    log("error when saving the user info");
                    out.write(MyJsonParser.SetUserInfoModifyResult(false,"invalid user info"));
                    out.flush();
                    out.close();
                    resp.flushBuffer();
                    abstruct_dao.close();
                    return;
                }

                // update the user info
                user_dao.update_user_byUnionID(user);


            } else {
                log("open id not match");
                out.write(MyJsonParser.SetUserInfoModifyResult(false,"do not have right to edit user info"));
                out.flush();
                out.close();
                resp.flushBuffer();
                abstruct_dao.close();
                return;
            }

            out.write(MyJsonParser.SetUserInfoModifyResult(true,"success"));
            out.flush();
            out.close();
            resp.flushBuffer();

        }

        abstruct_dao.close();

    }
}
