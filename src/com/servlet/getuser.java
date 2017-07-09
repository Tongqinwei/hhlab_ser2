package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.beans.user_brief;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@WebServlet(name = "getuser")
public class getuser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        String retString="[]";
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String tel=null;
        String session_id;
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        try {
            session_id = jsonObject.get("session_id").getAsString();
            try {
                tel = jsonObject.get("tel").getAsString();
            } catch (Exception e){
                tel=null;
            }
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }
        SessionUser sessionUser = SessionManager.getInstance().getUser(session_id);
        if (sessionUser.isAdministrator()) {
            //if (true) {
            int userid = user_dao.isExistByTel(tel);
            if (userid!=-1) {
                user User = user_dao.getUserByUserid(user_dao.isExistByTel(tel));
                JSONObject jsonString= JSONObject.fromObject(User);
                retString=jsonString.toString();
            }else {
                retString="{}";
            }

        }

        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
