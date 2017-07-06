package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.Reservation.ReservationTool;
import com.beans.ReservationOrder;
import com.beans.user;
import com.dao.ReservationDao;
import com.dao.user_dao;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by lee on 2017/7/6.
 *
 * @author: lee
 * create time: 下午12:26
 */
@WebServlet(name = "ReservationGetServlet")
public class ReservationGetServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));

        String session_id = null;

        //创建session，保存unionid
        SessionUser sessionUser = null;

        try {
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            // if there is error in json handling then send the error message
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error json"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        try {
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(session_id);
            if(sessionUser == null){
                throw new Exception("no such user");
            }
        } catch (Exception e){
            e.printStackTrace();
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"user not logged yet"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }


        JsonArray array = new JsonArray();
        try {
            List<ReservationOrder> list = ReservationDao.getOrderByUserID(sessionUser.getOpenID());
            for (ReservationOrder order : list){
                array.add(ReservationTool.OrderToJsonElement(order));
            }
        } catch (Exception e){
            e.printStackTrace();
            Writer out = response.getWriter();
            out.write(MyJsonParser.SetUserInfoModifyResult(false,"error when read the order"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(MyJsonParser.SetUserInfoModifyResult(true,array.getAsString()));
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
