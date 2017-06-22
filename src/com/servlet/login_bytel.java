package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import com.util._math;
import ssm.SMSHandlerManager;
import ssm.SMSHandlerPack.CodeSetter;
import ssm.SMSHandlerPack.SMSHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/15.
 */
@WebServlet(name = "login_bytel")
public class login_bytel extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
        String retString="failure";

//        String mode= request.getParameter("mode");
//        String tel = request.getParameter("TEL");
//        String captchare = request.getParameter("captchare");
//
        String mode= null;
        String tel = null;
        String captchare = null;
        String sessionID = null;

        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));

        try {
            mode = jsonObject.get("mode").getAsString();
            tel = jsonObject.get("TEL").getAsString();
            sessionID = jsonObject.get("session_id").getAsString();
        } catch (Exception e ){
            e.printStackTrace();
            Writer out = response.getWriter();
            out.write("failure: invalid json");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

//        HttpSession user_session = request.getSession();
        SessionManager manager = SessionManager.getInstance();
        SessionUser sessionUser = manager.getUser(sessionID);
        if(sessionUser == null){
            Writer out = response.getWriter();
            out.write("failure: not such user");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        } else  {

            if (mode.equals("1")){
                //读取用户信息
                user User;
                User = user_dao.getUserByUnionId(sessionUser.getOpenID());
                if (User != null){
                    retString="success";
                    sessionUser.ObjectMap.put("User",User);
                } else {
                    User = new user();
                    User.setUnionid(sessionUser.getOpenID());
//                    user_dao.add(User);

                    int userid=user_dao.isExistByTel(tel);
                    if (userid!=-1) {
                        retString="success";
//                        User.setUserid(UserIsExist);
                        user_dao.add(User);
                        sessionUser.ObjectMap.put("User",User);
                    } else {
                        retString = "failure: duplicated PhoneNumber";
                    }
                }

                if (retString.equals("success")){
                    String CaptchaString=_math.getCaptcha();
                    //retString+=CaptchaString;
                    sessionUser.ObjectMap.put("CaptchaString",CaptchaString);

                    //发短信验证手机
                    SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.SEND_CAPTCHA_TEXT);
                    smsHandler.setTelNum(tel);
                    smsHandler.setAttribute(CodeSetter.CodeTag,CodeSetter.setCode(CaptchaString));
                    smsHandler.send();

                }
            }else {
                try {
                    captchare = jsonObject.get("captchare").getAsString();
                } catch (Exception e){
                    e.printStackTrace();
                    captchare = "";
                }

                user User2= (user) sessionUser.ObjectMap.get("User");
                String CaptchaString2= (String) sessionUser.ObjectMap.get("CaptchaString");
                if (tel.equals(User2.getTel())&&CaptchaString2.equals(captchare)) retString="success";
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(retString);
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
