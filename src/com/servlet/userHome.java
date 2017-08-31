package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.beans.comment;
import com.dao.abstruct_dao;
import com.dao.comment_dao;
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
 * Created by hasee on 2017/8/31.
 */
@WebServlet(name = "userHome")
public class userHome extends HttpServlet {
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
        int userid;
        int _begin;
        int _end;

        try {
            userid = jsonObject.get("userid").getAsInt();
            _begin = jsonObject.get("_begin").getAsInt();
            _end = jsonObject.get("_end").getAsInt();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            int i=1;
            return;
        }


        comment[] comments = comment_dao.getComments(userid,_begin,_end,true);
        JSONArray book_json= JSONArray.fromObject(comments);
        retString = book_json.toString();


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
