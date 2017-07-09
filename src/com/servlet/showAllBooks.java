package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.book_brief;
import com.dao.abstruct_dao;
import com.dao.book_dao;
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
 * Created by hasee on 2017/7/9.
 */
@WebServlet(name = "showAllBooks")
public class showAllBooks extends HttpServlet {
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
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }
        try {
            _begin = jsonObject.get("begin").getAsInt();
        } catch (Exception e){
            _begin=1;
        }
        try {
            _end = jsonObject.get("end").getAsInt();
        } catch (Exception e){
            _end=_begin+20;
        }
        SessionUser sessionUser = SessionManager.getInstance().getUser(session_id);
        if (sessionUser.isAdministrator()) {
            book_brief[] book_briefs = book_dao.getAllBook_briefs(_begin,_end);
            JSONArray jsonString= JSONArray.fromObject(book_briefs);
            retString=jsonString.toString();
        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
