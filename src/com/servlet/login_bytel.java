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
        String retString="failure";

        String mode= null;
        String tel = null;
        String captchare = null;
        String sessionID = null;

        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        log("log in by tel : get json string :"+CreateSessionServlet.getBody(request));
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

                    int userid=user_dao.isExistByTel(tel);
                    if (userid!=-1) {
                        retString="success";
                        user_dao.add(User);
                        sessionUser.ObjectMap.put("User",User);

                    } else {
                        retString = "failure: duplicated PhoneNumber";
                    }
                }

                if (retString.equals("success")){
                    String CaptchaString=_math.getCaptcha();
                    //retString+=CaptchaString;
                    log("send captcha to user: "+ User.getTel() + "with captcha : " + CaptchaString);

                    sessionUser.ObjectMap.put("CaptchaString",CaptchaString);

                    //发短信验证手机
                    SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.SEND_CAPTCHA_TEXT);
                    smsHandler.setTelNum(tel);
                    smsHandler.setAttribute(CodeSetter.CodeTag,CodeSetter.setCode(CaptchaString));
                    smsHandler.send();

                }
            }else if(mode.contentEquals( "2")){
                try {
                    captchare = jsonObject.get("captchare").getAsString();
                } catch (Exception e){
                    e.printStackTrace();
                    captchare = "";
                }

                log("receive from user : " + tel + " AND Captcha: " + captchare);

                user User2= (user) sessionUser.ObjectMap.get("User");
                String CaptchaString2= (String) sessionUser.ObjectMap.get("CaptchaString");
                retString = "failure: captcha not match";
                if (tel.equals(User2.getTel())&&CaptchaString2.equals(captchare)) {
                    retString = "success";
                    user tem_user = user_dao.getUserByUnionId(sessionUser.getOpenID());

                    // update the database
                    if (tem_user.getTel() == null || tem_user.getTel().contentEquals("") || !tem_user.getTel().contentEquals(User2.getTel())){
                        log("update user tel from " + User2.getTel() + "  to " + tel);
                        user_dao.update_user_byUnionID(User2);
                    }

                    sessionUser.setOpenID(User2.getUnionid());
                    sessionUser.setCellPhone(tel);

                }
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
