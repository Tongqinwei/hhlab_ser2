package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.dao.*;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/27.
 */
@WebServlet(name = "addComment")
public class addComment extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";

        //取参数
        String isbn13 = request.getParameter("isbn13");
        String comment = request.getParameter("comment");
        String unionid = request.getParameter("unionid");
        double grade = 3.0;
        if (request.getParameter("grade")!=null){
            grade=Integer.parseInt(request.getParameter("grade"));
        }

        int userid = user_dao.getUserByUnionId(unionid).getUserid();

        if(comment_dao.add(unionid,isbn13,comment,grade,true))retString="add success";

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
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";

        //取参数
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        String unionid = null;
        String isbn13 = null;
        double grade = 0;
        String comment = null;
        String session_id = null;

        try {
            isbn13 = jsonObject.get("isbn13").getAsString();
            grade = Integer.parseInt(jsonObject.get("grade").getAsString());
            comment = jsonObject.get("comment").getAsString();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }
        unionid = SessionManager.getInstance().getUser(session_id).getOpenID();

        int userid = user_dao.getUserByUnionId(unionid).getUserid();

        if(comment_dao.add(unionid,isbn13,comment,grade,true))retString="add success";

        //添加用户行为
        ubhvor_dao.bhv_comment(userid,isbn13, (float) grade);

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
