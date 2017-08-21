package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/6/26.
 *
 * @author: lee
 * create time: 下午7:33
 */
@WebServlet(name = "GetUserINFOServlet")
public class GetUserINFOServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        abstruct_dao.connect();
        // 获取登录信息
        JsonObject jsonObject = null;
        String sessionID = null;
        SessionUser sessionUser = null;


        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");

        Writer out = response.getWriter();

        // try to get the json and the session user
        try{
            String data = CreateSessionServlet.getBody(request);
            log(" receive from data:" + data);

            jsonObject = MyJsonParser.String2Json(data);
            sessionID = jsonObject.get("session_id").getAsString();
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(sessionID);

            if(sessionUser == null){
                throw new Exception("no such a user and return the error info");
            }
        } catch (Exception e){
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"invalid request"));
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            abstruct_dao.close();
            return;
        }

        // get user info
        /*
            "user_detail":{
            "user_name":"",
            "birthday":"",
            "diploma":null,
            "email":"",
            "phone_num":"",
            "id_type":null,
            "id_num":"",
            "mail_address":"",
            "mail_code":""
        },*/

        user userData;

//        if(sessionUser.isAdministrator()){
//            userData = user_dao.getUserByUnionId(jsonObject.get("unionID").getAsString());
//        }
        userData = user_dao.getUserByUnionId(sessionUser.getOpenID());
        JsonObject object = new JsonObject();

        object.addProperty("user_name", userData.getName());
        object.addProperty("diploma",userData.getDegree());
        object.addProperty("email",userData.getEmail());
        object.addProperty("phone_num",userData.getTel());
        object.addProperty("id_type",userData.getCertificate());
        object.addProperty("id_num",userData.getCertificateid());
        object.addProperty("mail_address",userData.getAddress());
        object.addProperty("mail_code",userData.getPostcode());

        if(userData.getBirthday() != null){
            if(userData.getBirthday().contentEquals("0000-00-00")){
                object.addProperty("birthday", "");
            } else {
                object.addProperty("birthday",userData.getBirthday());
            }
        }


        object.addProperty("state",true);
        JsonObject retObject = new JsonObject();
//        retObject.addProperty("state",true);
        retObject.addProperty("state",true);
        retObject.addProperty("message", "success");
        retObject.addProperty("user_detail", object.toString());

        out.write(retObject.toString());
        out.flush();
        out.close();
        response.flushBuffer();
        abstruct_dao.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
