package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.google.gson.JsonObject;
import org.apache.zookeeper.Op;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/9/1.
 *
 * @author: lee
 * create time: 下午9:00
 */
public class CheckSession extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(404);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");

        String sessionID = null;
        String OpenID = null;

        String retString = "";


        try {
            String data = CreateSessionServlet.getBody(request);
            log("data");

            JsonObject object = MyJsonParser.String2Json(data);

            sessionID = object.get("session_id").getAsString();
            OpenID = object.get("open_id").getAsString();

        } catch (Exception e){
            e.printStackTrace();
            retString = MyJsonParser.retObject(false,"error json","",null).toString();
        }

        if(retString.contentEquals("")){
            try {
                SessionManager manager = SessionManager.getInstance();
                SessionUser user = manager.getUser(sessionID);
                if(user == null){
                    // 登录态超时

                    SessionUser sessionUser = new SessionUser();
                    sessionUser.setOpenID(OpenID);

                    manager.AddUser(sessionUser);

                    retString = MyJsonParser.retObject(true, "expired", sessionUser.getSessionID(), null).toString();


                } else {
                    // 登录有效
                    retString = MyJsonParser.retObject(true,"valid","",null).toString();
                }
            } catch (Exception e){
                retString = MyJsonParser.retObject(false,"error","",null).toString();
            }
        }


        Writer out = response.getWriter();
        out.write(retString);
        out.flush();
        response.flushBuffer();
    }
}
