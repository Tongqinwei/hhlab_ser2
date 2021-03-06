package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.comment;
import com.beans.home;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.comment_dao;
import com.dao.follow_dao;
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
        int userid1;
        int userid2;
        int _begin;
        int _end;
        String session_id;
        home Home=new home();

        try {
            userid2 = jsonObject.get("userid").getAsInt();
            _begin = jsonObject.get("_begin").getAsInt();
            _end = jsonObject.get("_end").getAsInt();
            Home.setOwner(user_dao.getUserByUserid(userid2));
            Home.setComments(comment_dao.getComments(userid2,_begin,_end,true));
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            int i=1;
            return;
        }

        try {
            session_id = jsonObject.get("session_id").getAsString();
            String unionid = SessionManager.getInstance().getUser(session_id).getOpenID();
            user visitor =user_dao.getUserByUnionId(unionid);
            userid1 = visitor.getUserid();
            Home.setVisitor(visitor);
            if (userid1==userid2) {
                Home.setHisOwn(true);
                Home.setFollower(false);
            } else {
                Home.setHisOwn(false);
                Home.setFollower(follow_dao.isFollower(userid1,userid2));
            }
        } catch (Exception e){
            Home.setVisitor(null);
            Home.setHisOwn(false);
            Home.setFollower(false);
        }


        JSONArray book_json= JSONArray.fromObject( Home);
        retString = book_json.toString();


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
