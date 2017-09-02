package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
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
 * Created by hasee on 2017/9/2.
 */
@WebServlet(name = "addComment")
public class follow extends HttpServlet {
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
        String mode;//add,del,show
        int userid1=0;
        int userid2=0;
        String session_id;

        try {
            mode = jsonObject.get("mode").getAsString();

            if (mode.equals("add")||mode.equals("del")){
                userid2 = jsonObject.get("userid").getAsInt();
            }

            session_id = jsonObject.get("session_id").getAsString();
            String unionid = SessionManager.getInstance().getUser(session_id).getOpenID();
            user visitor =user_dao.getUserByUnionId(unionid);
            userid1 = visitor.getUserid();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            int i=2;
            return;
        }

        if (mode.equals("show")){
            //JSONArray book_json= JSONArray.fromObject(Home);
            //retString = book_json.toString();
        }
        else if (mode.equals("add")){
            follow_dao.add(userid1,userid2);
            retString = "finish";
        }
        else if (mode.equals("del")){
            follow_dao.del(userid1,userid2);
            retString = "finish";
        }


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
