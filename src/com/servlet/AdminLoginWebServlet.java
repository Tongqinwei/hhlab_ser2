package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.adminUser;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.admin_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午11:56
 */
@WebServlet(name = "AdminLoginWebServlet")
public class AdminLoginWebServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/html;charset=UTF-8");
        JsonObject jsonObject = null;
        String logName = null;
        String passWord= null;

        try {
            String recData = CreateSessionServlet.getBody(request);
            log(recData);
            jsonObject = MyJsonParser.String2Json(recData);
            logName = jsonObject.get("log_name").getAsString();
            passWord = jsonObject.get("password").getAsString();
        } catch (Exception e){
            // if there is error in json handling then send the error message
            e.printStackTrace();
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error json type"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }


//        try {
//            // 尝试解密
//
////            passWord = RASTool.decryptByPrivateKey(passWord.getBytes(),keyManager.getRsaManager().getPrivateKey().getEncoded());
//
//
//        } catch (Exception e){
//            e.printStackTrace();
//            Writer out = response.getWriter();
//            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error while decrypting"));
//            out.flush();
//            out.close();
//            response.flushBuffer();
//            return;
//        }



        adminUser adminUser ;
        try {
            adminUser = admin_dao.getAdminByLogName(logName);
            if(adminUser == null || !adminUser.isPasswordMatch(passWord)){
                throw new Exception("不存在该管理员 ： "+logName);
            }

        } catch (Exception e){
            // if there is error in json handling then send the error message
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"no such user or password not match"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        SessionUser user = new SessionUser();
        SessionManager manager = SessionManager.getInstance();

        abstruct_dao.connect();
        if(user_dao.isExistByUnionid(adminUser.getBindOpenID()) != -1){
            user User = user_dao.getUserByUnionId(adminUser.getBindOpenID());
            if(User != null){
                user.setOpenID(adminUser.getBindOpenID());
                user.setPower(10);
                user.setCellPhone(User.getTel());
                manager.AddUser(user);
            }
        } else {
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"no binded user in database"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        Cookie cookie = new Cookie("sessionCookie",user.getSessionID());
        response.addCookie(cookie);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(MyJsonParser.SetUserInfoModifyResult(true,"success"));
        out.flush();
        out.close();
        response.flushBuffer();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }
}
