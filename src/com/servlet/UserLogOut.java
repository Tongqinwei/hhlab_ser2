package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/9/4.
 *
 * @author: lee
 * create time: 下午8:26
 */
public class UserLogOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();

        String sessionID = null;
        try {
//          check request data
            String data = CreateSessionServlet.getBody(request);
            JsonObject jsonObject = MyJsonParser.String2Json(data);
            sessionID = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error json"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }


        try {

            SessionManager manager = SessionManager.getInstance();
            SessionUser user = manager.getUser(sessionID);
            if(user == null){
                throw new Exception("log out error no such user");
            } else {
                manager.removeUser(sessionID);
            }

        } catch (Exception e){
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"No such user"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }


        out.write(MyJsonParser.SetUserInfoModifyResult(true,"log out success"));
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
