package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user_brief;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.orderForm_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/30.
 * 用于管理员端显示所有用户信息
 */
@WebServlet(name = "showAllUsers")
public class showAllUsers extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        int _begin=1;
        int _end=20;
        if (request.getParameter("begin")!=null) _begin= Integer.parseInt( request.getParameter("begin"));
        else _begin=1;
        if (request.getParameter("end")!=null) _end= Integer.parseInt( request.getParameter("end"));
        else _end=_begin+20;
        user_brief[] user_briefs = user_dao.getAllUser_bf(_begin,_end);
        JSONArray jsonString= JSONArray.fromObject(user_briefs);
        String retString=jsonString.toString();
        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        String retString="[]";
        abstruct_dao.connect();
        Writer out = response.getWriter();
        int _begin=1;
        int _end=20;
        String session_id;
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        try {
            _begin = jsonObject.get("begin").getAsInt();
            _end = jsonObject.get("end").getAsInt();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }
        SessionUser sessionUser = SessionManager.getInstance().getUser(session_id);
        if (sessionUser.isAdministrator()) {
            user_brief[] user_briefs = user_dao.getAllUser_bf(_begin,_end);
            JSONArray jsonString= JSONArray.fromObject(user_briefs);
            retString=jsonString.toString();
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
