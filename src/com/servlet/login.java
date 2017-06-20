package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.user_dao;

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
        String unionid = request.getParameter("UNIONID");
        String session_id= request.getParameter("session_id");
        String retString="failure";

        //创建session，保存unionid
        SessionManager manager = SessionManager.getInstance();
        SessionUser sessionUser = manager.getUser(session_id);
        if(sessionUser == null){
            retString="failure:cannot get session";
        }else {
            //可以获得session
            sessionUser.setUnionID(unionid);
            int UserIsExist=user_dao.isExistByUnionid(unionid);
            if (UserIsExist!=-1){
                retString="success";
            }else {
                user User= new user();
                User.setUnionid(unionid);
                user_dao.add(User);
                int userid=user_dao.isExistByUnionid(unionid);
                if (userid!=-1){
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
