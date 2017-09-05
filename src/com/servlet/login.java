package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.admin_dao;
import com.dao.ubhvor_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/15.
 */
@WebServlet(name = "login")
public class login extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
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
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        String retString="failure";

        //创建session，保存unionid
        SessionManager manager = SessionManager.getInstance();
        SessionUser sessionUser = manager.getUser(session_id);
        if(sessionUser == null){
            retString="failure:cannot get session";
        }else {
            //可以获得session

            if(!sessionUser.getOpenID().contentEquals(openID)){
//                if the open id is not match
                Writer out = response.getWriter();
                out.write("failure: openID is not match");
                out.flush();
                out.close();
                response.flushBuffer();
                return;
            }

            int UserIsExist=user_dao.isExistByUnionid(openID);
            if (UserIsExist!=-1){
                retString="success";
            }else {
                user User= new user();
                User.setUnionid(openID);
                user_dao.add(User);
                int userid=user_dao.isExistByUnionid(openID);
                if (userid!=-1){
                    //ubhvor_dao.change(userid,"9787305130779",0);
                    //ubhvor_dao.flush();
                    retString="success";
                }
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
}
