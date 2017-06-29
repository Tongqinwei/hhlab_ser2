package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.admin_dao;
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
 * Created by lee on 2017/6/30.
 *
 * @author: lee
 * create time: 上午2:05
 */
@WebServlet(name = "AdminLoginWeChatServlet")
public class AdminLoginWeChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));

        String openID = null;
        String session_id= null;

        try {
            openID = jsonObject.get("open_id").getAsString();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            // if there is error in json handling then send the error message
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false, "json parse error"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        String retString="null";

        //创建session，保存unionid
        SessionManager manager = SessionManager.getInstance();
        SessionUser sessionUser = manager.getUser(session_id);
        if(sessionUser == null){
            retString = MyJsonParser.SetUserInfoModifyResult(false, "session is invalid");
        }else {
            //可以获得session

            if(!sessionUser.getOpenID().contentEquals(openID)){
//                if the open id is not match
                Writer out = response.getWriter();
                out.write(MyJsonParser.SetUserInfoModifyResult(false, "session not match"));
                out.flush();
                out.close();
                response.flushBuffer();
                return;
            }

            int UserIsExist= user_dao.isExistByUnionid(openID);
            if (UserIsExist!=-1 && admin_dao.getAdminByOpenID(openID) != null){
                retString = MyJsonParser.SetUserInfoModifyResult(true, "success");
                sessionUser.setPower(10);
            }else {
                retString = MyJsonParser.SetUserInfoModifyResult(false, "not such user");
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(retString);
        out.flush();
        out.close();
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }
}
