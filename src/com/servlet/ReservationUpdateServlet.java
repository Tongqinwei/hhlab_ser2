package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.ReservationOrder;
import com.beans.book_brw;
import com.beans.orderForm;
import com.beans.storage_book;
import com.dao.ReservationDao;
import com.dao.orderForm_dao;
import com.dao.storage_book_dao;
import com.google.gson.JsonObject;
import com.util.OF_util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/7/6.
 *
 * @author: lee
 * create time: 下午12:27
 */
@WebServlet(name = "ReservationUpdateServlet")
public class ReservationUpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));

        String session_id = null;
        String order_id = null;
        String action = null;

        //创建session，保存unionid
        SessionUser sessionUser = null;

        try {
            session_id = jsonObject.get("session_id").getAsString();
            order_id = jsonObject.get("order_id").getAsString();
            action = jsonObject.get("action").getAsString();

            switch (action) {
                case "cancel":
                    break;
                case "confirm":
                    break;
                case "delete":
                    break;
                default:
                    throw new Exception("error action!");
            }
        } catch (Exception e) {
            // if there is error in json handling then send the error message
            Writer out = response.getWriter();
            e.printStackTrace();
            out.write(MyJsonParser.SetUserInfoModifyResult(false, "error json"));
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        String retString = MyJsonParser.SetUserInfoModifyResult(false, "error");
        try {
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(session_id);
            if (sessionUser == null) {
                // 检查用户登录
                retString = MyJsonParser.SetUserInfoModifyResult(false, "no such user");
                throw new Exception("no user");
            }

            ReservationOrder order = ReservationDao.getOrderbyOrderID(order_id);
            if (order == null) {
                // 检查订单是否存在
                retString = MyJsonParser.SetUserInfoModifyResult(false, "order not existed");
                throw new Exception("no order");
            }


            if (order.getUser_id().contentEquals(sessionUser.getOpenID())) {
                retString = MyJsonParser.SetUserInfoModifyResult(false, "not your order");
                throw new Exception("order owner error");
            }

            switch (action) {
                case "cancel": {
                    // 取消订单
                    if (order.getState() < 3) {

                        if (order.getState() == 2) {
                            storage_book book = storage_book_dao.getStorage_book(order.getBarCode());
                            if (book.getBook_state().contentEquals("2")) {
                                storage_book_dao.updateState(book.getBook_id(), 4);
                            }
                        }

                        order.setState(5);
                        // 更新订单，标记为删除
                        ReservationDao.updateOrder(order);
                        retString = MyJsonParser.SetUserInfoModifyResult(true, "cancel order success！");
                    } else {
                        retString = MyJsonParser.SetUserInfoModifyResult(false, "cannot cancel");
                    }
                    break;
                }

                case "confirm": {
                    // 确认订单
                    if (order.getState() == 2) {
                        // 生成订单

                        storage_book book = storage_book_dao.getStorage_book(order.getBarCode());
                        //至少有一本书
                        String orderid = OF_util.getNewOrderid();
                        OF_util of_util = new OF_util(sessionUser.getOpenID(), orderid);
                        book_brw book1 = new book_brw();
                        book1.setBarcode(book.getBook_id());
                        of_util.add(book1);

                        orderForm OrderForm = of_util.toOrderForm();
                        orderForm_dao OFD = new orderForm_dao(OrderForm);

                        if (!OFD.add(true)) {
                            retString = MyJsonParser.SetUserInfoModifyResult(true, "success");
                            order.setState(3);
                            // 更新订单，标记为正常完成
                            ReservationDao.updateOrder(order);

                        } else {
                            retString = MyJsonParser.SetUserInfoModifyResult(false, "failed when confirmed");
                        }

                    } else {
                        retString = MyJsonParser.SetUserInfoModifyResult(false, "cannot confirm");
                    }

                }
                break;

                case "delete":{
                    // 删除订单
                    if (order.getState() == 3 || order.getState() == 4) {
                        // 删除订单
                        order.setState(5);
                        ReservationDao.updateOrder(order);
                        retString = MyJsonParser.SetUserInfoModifyResult(true, "success");

                    } else {
                        retString = MyJsonParser.SetUserInfoModifyResult(false, "cannot delete");
                    }
                }
                    break;
                default:
                    throw new Exception("error action!");
            }

        } catch (Exception e) {
            Writer out = response.getWriter();
            e.printStackTrace();
            out.write(retString);
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();
        out.write(retString);
        out.flush();
        out.close();
        response.flushBuffer();
    }



    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);

    }
}
