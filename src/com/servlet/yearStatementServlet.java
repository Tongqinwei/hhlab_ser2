package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.year_statement;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.dao.year_statement_dao;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/8/27.
 */
@WebServlet(name = "yearStatementServlet")
public class yearStatementServlet extends HttpServlet {
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
        String key = request.getParameter("key");

        if (key.equals("960522")){
            year_statement_dao.init();
            retString="已重新计算统计信息";
        }

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
        String session_id = null;

        try {
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

        String name=user_dao.getTrueNameByUserid(userid);

        year_statement ans= year_statement_dao.getYearStatementByUserid(userid);
        if (ans!=null){
            JSONObject book_json= JSONObject.fromObject(ans);
            retString = book_json.toString();
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
