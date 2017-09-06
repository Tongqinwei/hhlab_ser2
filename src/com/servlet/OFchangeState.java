package com.servlet;

import com.Login.Bean.SessionUser;
import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.Reservation.ReservationTool;
import com.beans.orderForm;
import com.dao.abstruct_dao;
import com.dao.orderForm_dao;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;
import ssm.SMSHandlerManager;
import ssm.SMSHandlerPack.CodeSetter;
import ssm.SMSHandlerPack.OrderSetter;
import ssm.SMSHandlerPack.SMSHandler;
import ssm.SMSHandlerPack.TimeSetter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/29.
 * 用于订单状态改变
 */
@WebServlet(name = "OFchangeState")
public class OFchangeState extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setStatus(404);
//        response.setContentType("text/html;charset=UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type","text/html;charset=UTF-8");
//        response.setContentType("application/json");
//        abstruct_dao.connect();
//        Writer out = response.getWriter();
//        String retString="failure";
//
//        //取参数
//        String newState = request.getParameter("newState");
//        String orderid = request.getParameter("orderid");
//
//        JSONArray jsonString=null;
//        if ("comfirm".equals(newState)){
//            if (orderForm_dao.stateChange_noConfirm2comfirm(orderid)) retString="success change to "+newState+".";
//        }else if ("pay".equals(newState)){
//            if (orderForm_dao.stateChange_confirm2pay(orderid)) retString="success change to "+newState+".";
//        }else if ("return".equals(newState)){
//            if (orderForm_dao.stateChange_pay2finish(orderid)) retString="success change to "+newState+".";
//        }else if ("failure".equals(newState)){
//            if (orderForm_dao.stateChange_2failure(orderid)) retString="success change to "+newState+".";
//        }else {
//            retString= String.format("Can not find the newState %s.", newState);
//        }

//        out.write(retString);
//        //结尾
//        out.flush();
//        out.close();
//        response.flushBuffer();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();

        SessionUser sessionUser = null;
        String orderid = null;
        String newState = null;
        try {
            String data = CreateSessionServlet.getBody(request);
            String sessionID = null;
            JsonObject jsonObject = MyJsonParser.String2Json(data);
            sessionID = jsonObject.get("session_id").getAsString();
            orderid = jsonObject.get("orderid").getAsString();
            newState = jsonObject.get("newState").getAsString();
            SessionManager manager = SessionManager.getInstance();
            sessionUser = manager.getUser(sessionID);
            System.out.println(sessionUser.getSessionID().toLowerCase());
        } catch (Exception e){
            e.printStackTrace();
            out.write(ReservationTool.returnAsJson(false,"json error or no such user!","请尝试重新登录",null));
            out.flush();
            response.flushBuffer();
            return;
        }

        String retString = ReservationTool.returnAsJson(false, "fail to modify", "修改失败", null);
        try {
            orderForm form = orderForm_dao.getOrderFormByOrderid(orderid);
            if(form.getUnionid().contentEquals(sessionUser.getOpenID()) || sessionUser.isAdministrator()){
                // check is the same order
                switch (newState){
                    case "confirm" :
                        if (orderForm_dao.stateChange_noConfirm2comfirm(orderid)){
                            retString = ReservationTool.returnAsJson(true,"success to "+newState, "修改成功",null);
                            SMSHandler handler = new SMSHandler(SMSHandlerManager.ORDER_CONFIRMED_TEXT);
                            handler.setAttribute(OrderSetter.OrderTag,OrderSetter.setOrder(orderid));
                            handler.send();
                        }
                        break;
                    case "pay" :
                        if (orderForm_dao.stateChange_confirm2pay(orderid)){
                            retString = ReservationTool.returnAsJson(true,"success to "+newState, "修改成功",null);
                            SMSHandler handler = new SMSHandler(SMSHandlerManager.ORDER_BORROW_SUCCESS_TEXT);
                            handler.setAttribute(OrderSetter.OrderTag,OrderSetter.setOrder(orderid));
                            handler.setAttribute(TimeSetter.TimeTag,TimeSetter.setTime(ReservationTool.get30DaysLater()));
                            handler.send();
                        }
                        break;
                    case "return":
                        if (orderForm_dao.stateChange_pay2finish(orderid)){
                            retString = ReservationTool.returnAsJson(true,"success to "+newState, "修改成功",null);

                        }
                        break;
                    case "failure" :
                        if (orderForm_dao.stateChange_2failure(orderid)){
                            retString = ReservationTool.returnAsJson(true,"success to "+newState, "修改成功",null);
                        }
                        break;
                    default:
                        retString = ReservationTool.returnAsJson(false,"no such "+newState + " state", "修改失败",null);
                        break;
                }

            } else {
                throw new Exception("power error");
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            out.write(retString);
            out.flush();
            response.flushBuffer();
        }


    }
}
