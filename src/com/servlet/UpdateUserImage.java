package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.UserImageDao;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/8/31.
 *
 * @author: lee
 * create time: 下午7:09
 */
public class UpdateUserImage extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        abstruct_dao.connect();
        // 获取登录信息
        JsonObject jsonObject = null;
        SessionUser sessionUser = null;
        String imgURL = null;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();

        JsonObject object = null;
        // try to get the json and the session user
        try{

            String data = CreateSessionServlet.getBody(request);
            log("request body data + " + data);

            jsonObject = MyJsonParser.String2Json(data);

            String session =  jsonObject.get("session_id").getAsString();
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(session);

            if(sessionUser == null){
                throw new Exception("get User error");
            }

            imgURL = jsonObject.get("avatar").getAsString();

        } catch (Exception e){
            e.printStackTrace();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error request"));
            out.flush();
            response.flushBuffer();
            return;
        }

        try {

            user userInfo = user_dao.getUserByUnionId(sessionUser.getOpenID());
            if (userInfo.getUnionid().contentEquals(sessionUser.getOpenID())){
                // match user
                UserImageDao.UpdateUserImage(userInfo.getUnionid(),imgURL);
            }

        } catch (Exception e){
            e.printStackTrace();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"failed"));
            out.flush();
            response.flushBuffer();
            return;
        }

        out.write(MyJsonParser.SetUserInfoModifyResult(true,"success"));
        out.flush();
        response.flushBuffer();
        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }
}
