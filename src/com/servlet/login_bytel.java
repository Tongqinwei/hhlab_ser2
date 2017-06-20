package com.servlet;

import com.beans.user;
import com.dao.user_dao;
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
        response.setContentType("text/html;charset=UTF-8");
        String mode= request.getParameter("mode");
        String tel = request.getParameter("TEL");
        String captchare = request.getParameter("captchare");
        String retString="failure";
        HttpSession user_session = request.getSession();
        if (mode.equals("1")){

            //读取用户信息
            user User = new user();
            User.setTel(tel);
            //创建session，保存user
            int UserIsExist= user_dao.isExistByTel(tel);
            if (UserIsExist==-1){
                retString="success";
                User.setUserid(UserIsExist);
                user_session.setAttribute("User",User);
            }else
                {
                user_dao.add(User);
                int userid=user_dao.isExistByTel(tel);
                if (userid!=-1) {
                    retString="success";
                    User.setUserid(UserIsExist);
                    user_session.setAttribute("User",User);
                }
            }
            if (retString.equals("success")){
                String CaptchaString=_math.getCaptcha();
                //retString+=CaptchaString;
                user_session.setAttribute("CaptchaString",CaptchaString);

                //发短信验证手机
                SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.SEND_CAPTCHA_TEXT);
                smsHandler.setTelNum(tel);
                smsHandler.setAttribute(CodeSetter.CodeTag,CodeSetter.setCode(CaptchaString));
                smsHandler.send();

            }
        }else {
            user User2= (user) user_session.getAttribute("User");
            String CaptchaString2= (String) user_session.getAttribute("CaptchaString");
            if (tel.equals(User2.getTel())&&CaptchaString2.equals(captchare)) retString="success";
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
