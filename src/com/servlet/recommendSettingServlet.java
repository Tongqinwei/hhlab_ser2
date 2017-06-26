package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/6/27.
 *
 * @author: lee
 * create time: 上午2:54
 */
@WebServlet(name = "recommendSettingServlet")
public class recommendSettingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        abstruct_dao.connect();
        // 获取登录信息
        JsonObject jsonObject = null;
        String sessionID = null;
        SessionUser sessionUser = null;
        int frequency = 0;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");

        Writer out = response.getWriter();

        try {
            jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
            sessionID = jsonObject.get("session_id").getAsString();
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(sessionID);
            frequency = jsonObject.get("frequence").getAsInt();

            if(sessionUser == null){
                throw new Exception(" the user is not in the manager");
            }
        } catch (Exception e){
            e.printStackTrace();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error json"));
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            abstruct_dao.close();
            return;
        }

        String retString = null;
        if(user_dao.isExistByUnionid(sessionUser.getOpenID()) != -1){
            // exist the user
            user dbUser = user_dao.getUserByUnionId(sessionUser.getOpenID());
            dbUser.setRecommendFrequency(frequency);

            log("update user recommendation frequency to " + dbUser.getRecommendFrequency());
            user_dao.update_user_byUnionID(dbUser);

            retString = MyJsonParser.SetUserInfoModifyResult(true,"success");
        } else {
            retString = MyJsonParser.SetUserInfoModifyResult(false,"user not found");
        }

        out.write(retString);
        out.flush();
        out.close();
        response.flushBuffer();
        abstruct_dao.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
