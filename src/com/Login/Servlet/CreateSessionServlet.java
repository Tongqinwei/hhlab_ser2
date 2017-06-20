package com.Login.Servlet;


import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Handler.WechatSessionHandler;
import com.Login.Sessions.SessionManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by lee on 2017/6/17.
 *
 * @author: lee
 * create time: 下午1:43
 */
@WebServlet(name = "CreateSessionServlet")
public class CreateSessionServlet extends HttpServlet {

    public static String getBody(HttpServletRequest request) throws IOException {
        // get the body of the request object to a string

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // get the request pay load
        String session_code = getBody(request);

        // get response writer
        Writer out = response.getWriter();
        response.setContentType("application/json");

        if(!session_code.contentEquals("")){
            // if the code is not send return the error json
            session_code = MyJsonParser.CodeJsonToString(session_code);
            log("receive session code from js is " + session_code);

            if(session_code.contentEquals("")){
//                failed to parse
                out.write(MyJsonParser.GetSessionError());
            } else {
//                session code get success

                String result = WechatSessionHandler.getSessionKey(session_code);
                log("receive from weChat server " + result);
                JsonObject jsonObject = MyJsonParser.String2Json(result);

                JsonElement element = jsonObject.get("openid");

                if(element != null){
                    // 成功调用到微信的状态，为用户建立一个session 对象 方便进行管理
                    log("receive session key success! ");

                    SessionUser user = new SessionUser();

                    element = jsonObject.get("openid");
                    user.setOpenID(element.getAsString());

                    element = jsonObject.get("session_key");
                    user.setSessionKey(element.getAsString());

                    SessionManager manager = SessionManager.getInstance();
                    manager.AddUser(user);
                    // 返回成功的json
                    out.write(MyJsonParser.GetSessionSuccess(user.getSessionID()));

                } else {
                    out.write(MyJsonParser.GetSessionError());
                }
            }

        } else {
            out.write(MyJsonParser.GetSessionError());
        }

        out.flush();
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(404);
    }
}
