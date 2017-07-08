package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.book;
import com.beans.book_brief;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.ubhvor_dao;
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
 * Created by hasee on 2017/5/29.
 * 用于首页推荐
 */
@WebServlet(name = "recommend_index")
public class recommend_index extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//取参数
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        String unionid = null;
        String session_id = null;
        abstruct_dao.connect();
        book[] Books = book_dao.getRecommendBook_index(1,5);
        book_brief[] Books_b_hot = new book_brief[Books.length];
        for (int  i=0;i< Books_b_hot.length;i++) {
            Books_b_hot[i]=Books[i].toBook_brief("");
        }
        String recommendString = "";

        try {
            session_id = jsonObject.get("session_id").getAsString();
            unionid = SessionManager.getInstance().getUser(session_id).getOpenID();
            int userid = user_dao.getUserByUnionId(unionid).getUserid();
            String[] strings=ubhvor_dao.getUserRecommendedIsbn13s(userid,1,6);
            book_brief[] Books_b_rec = new book_brief[strings.length];
            for (int  i=0;i< Books_b_rec.length;i++) {
                Books_b_rec[i]=book_dao.getBookByIsbn13(strings[i]).toBook_brief("");
            }
            recommendString=","+JSONArray.fromObject(Books_b_rec).toString();
        } catch (Exception e){

        }

        JSONArray hot_json = JSONArray.fromObject(Books_b_hot);
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(String.format("{\"hot\":%s%s}", hot_json.toString(), recommendString));
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
