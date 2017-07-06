package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.Reservation.ReservationManager;
import com.Reservation.ReservationTool;
import com.beans.ReservationOrder;
import com.beans.user;
import com.dao.ReservationDao;
import com.dao.user_dao;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;

/**
 * Created by lee on 2017/7/6.
 *
 * @author: lee
 * create time: 下午12:40
 */
@WebServlet(name = "ReservationCreateServlet")
public class ReservationCreateServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        String session_id = null;
        String isbn = null;

        //创建session，保存unionid
        SessionUser sessionUser = null;

        try {
            JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
            session_id = jsonObject.get("session_id").getAsString();
            isbn = jsonObject.get("isbn").getAsString();
        } catch (Exception e){
            // if there is error in json handling then send the error message
            Writer out = response.getWriter();
            out.write(ReservationTool.returnAsJson(false,"error json","通信错误！",null));
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
            out.write(ReservationTool.returnAsJson(false,"no such user", "您还没有登录！",null));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }


        user User = null;
        String retString = ReservationTool.returnAsJson(false, "error when check info", "您当期图书不可预订", null);
        try {
            if(ReservationTool.isReservable(isbn)) {
                User = user_dao.getUserByUnionId(sessionUser.getOpenID());
                if (User.getTel().length() == 0) {
                    retString = ReservationTool.returnAsJson(false, "userInfo incomplete", "您的信息不完善，无法预订", null);
                    throw new Exception("user info error");
                }
            } else {
                retString = ReservationTool.returnAsJson(false, "book cannot load", "图书不可预订", null);
                throw new Exception("cannot book");
            }
        } catch (Exception e){
            e.printStackTrace();
            Writer out = response.getWriter();
            out.write(retString);
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        retString = ReservationTool.returnAsJson(false, "error when create order", "预订订单创建失败", null);
        try {
            List<ReservationOrder> list = ReservationDao.getOrderByUserID(sessionUser.getOpenID());

            for (ReservationOrder order : list){
                if(order.getState() == 1 && order.getISBN().contentEquals(isbn)){
                    // 检查出现重复的预订
                    retString = ReservationTool.returnAsJson(false,"duplicated reservation", "不允许重复预订",null);
                    throw new Exception("duplicated reservation");
                }
            }

//            storage_book book = ReservationTool.reserveBookIfAvailable(isbn);
            ReservationDao.inputReservation(sessionUser.getOpenID(),isbn,new Date());

            list.clear();
            list = ReservationDao.getOrderByUserID(sessionUser.getOpenID());
            for (ReservationOrder order : list){
                if(order.getState() == 1 && order.getISBN().contentEquals(isbn)){
                    // 查询到订单后，发送短信
                    ReservationTool.sendAddIntoList(order,User.getTel());
                }
            }

            // 唤醒
            ReservationManager.update();
            retString = ReservationTool.returnAsJson(true,"success","预订成功",null);
        } catch (Exception e){
            Writer out = response.getWriter();
            out.write(retString);
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            return;
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
