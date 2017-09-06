package com.Reservation;

import com.beans.ReservationOrder;
import com.beans.book;
import com.beans.storage_book;
import com.dao.ReservationDao;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.storage_book_dao;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.util._math;
import ssm.SMSHandlerManager;
import ssm.SMSHandlerPack.*;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by lee on 2017/7/5.
 *
 * @author: lee
 * create time: 下午3:08
 */
public class ReservationTool {

    public synchronized static storage_book reserveBookIfAvailable(String ISBN){
       /*
        * 如果可以的话，就执行预留一本图书的操作,否则返回空
        * */
        storage_book[] Storage_books =  storage_book_dao.getStorage_books(ISBN);
        storage_book result = null;

        if(storage_book_dao.count_transcript(ISBN) > 0){
            for (storage_book book: Storage_books) {
                if(book.getBook_state().contentEquals("4")){
                    storage_book_dao.updateState(book.getBook_id(),2);
                    result = storage_book_dao.getStorage_book(book.getBook_id());
                    break;
                }
            }
        }
        abstruct_dao.close();
        return result;
    }

    public static boolean isReservable(String ISBN){
        /*
        * 查询图书是否可以预订，如果存在一本图书可以借出，则返回不可预约
        * */
        storage_book[] Storage_books =  storage_book_dao.getStorage_books(ISBN);
        boolean result = false;

        if(storage_book_dao.count_transcript(ISBN) > 0){
            result = true;
            for (storage_book book: Storage_books) {
                if(book.getBook_state().contentEquals("4")){
                    result = false;
                    break;
                }
            }
        }
        abstruct_dao.close();
        return result;
    }

    public static String state2String(int state){

        /**
         * Mark == 1 : 正在预订
         * Mark == 2 : 预订成功，正在保留
         * Mark == 3 : 约定正常完成
         * Mark == 4 : 约定超时未确认，失效
         * Mark == 5 : 约定异常失效或删除
         */
        switch (state){
            case 1: return "waiting";
            case 2: return "ready";
            case 3: return "finished";
            case 4: return "out_of_time";
            case 5: return "failed";
            default:return "failed";
        }
    }

    public static void listSortToArray(List<ReservationOrder> list){
        /**
         * 将订单按照订单状态降序排列，二级排序按预订时间排序
         * */
        //            升序排列：obj1-obj2>0的话返回1，说明按照从小到大排序
        list.sort((o1, o2) -> {
            if(o1.getState() == o2.getState()){
                long tem = o1.getReserveTime().getTime() - o2.getReserveTime().getTime();
                return tem > 0 ? 1 : -1;
            } else {
                return o1.getState() - o2.getState();
            }
        });
    }

    public static int getPeopleWaiting(String User_id, String ISBN){
        /**
         * 查询用户预订的订单前，有多少位用户在等待
         * */
        List<ReservationOrder> list = ReservationDao.getOrderByISBN(ISBN);

        listSortToArray(list);

        int i = 0;

        for (ReservationOrder order: list) {
            if(!order.getUser_id().contentEquals(User_id) && order.getState() <= 2){
                i++;
            } else if(order.getUser_id().contentEquals(User_id) && order.getState() == 1){
                break;
            }
        }
        return i;
    }

    public static JsonElement OrderToJsonElement(ReservationOrder order){
        /**
         * 将订单处理为前端需要的JSON格式
         * */
        JsonObject object = new JsonObject();
        object.addProperty("id", order.getOrderID());
        object.addProperty("order_state", state2String(order.getState()));
        object.addProperty("people_waiting", getPeopleWaiting(order.getUser_id(),order.getISBN()));

        book Book = book_dao.getBookByIsbn13(order.getISBN());
        abstruct_dao.close();
        JsonObject bookJson = new JsonObject();
        bookJson.addProperty("book_title", Book.getTitle());
        if(order.getBarCode() == null){
            bookJson.addProperty("book_content", "图书ISBN编码为 ： " + order.getISBN());
        } else {
            bookJson.addProperty("book_content", "图书条码号为 ： " + order.getBarCode());
        }
        bookJson.addProperty("book_img_url", Book.getImage());
        bookJson.addProperty("book_url", order.getISBN());

        object.add("books", bookJson);

        JsonArray orderInfo = new JsonArray();
        switch (order.getState()){
            case 1: break;
            case 2:
                orderInfo.add("预订已成功！请在" + _math.date2TimeFormat(order.getStartTime()) + "前，到图书馆管理员处确认您的订单，超时您的预订将失效");
                break;
            case 3:
                orderInfo.add("您的预订已完成！图书借阅订单已经生成，请于管理员联系");
                break;
            case 4:
                orderInfo.add("订单已失效，失效原因：超过："+_math.date2TimeFormat(order.getStartTime()) + " 未确认");
                break;
            case 5:
                orderInfo.add("已删除的订单");
                break;
        }
        orderInfo.add("预订创建时间 ： " + _math.date2TimeFormat(order.getReserveTime()));
        orderInfo.add("订单编号 ： " + order.getOrderID());

        object.add("order_info",orderInfo);
        return object;
    }

    public static void sendAddIntoList(ReservationOrder order, String tel){
        /**
         * 发送加入预订序列短信
         * "【嚯哈借阅伴侣】您预订的#book#图书，已加入预订队列，当前预订队列中有#people_waiting#人在等待，系统将第一时间通知您的预订结果，感谢您的使用";
         * */
        SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.RESERVATION_ADD_TEXT);
        smsHandler.setTelNum(tel);
        smsHandler.setAttribute(BookSetter.BookTag, BookSetter.setBook(book_dao.getBookByIsbn13(order.getISBN()).getTitle()));
        smsHandler.setAttribute(PeopleSetter.PeopleTag,PeopleSetter.setPeopleWaiting(getPeopleWaiting(order.getUser_id(),order.getISBN())));
        smsHandler.send();
    }

    public static void sendBookSuccess(ReservationOrder order, String tel){
        /**
         * 发送预订成功短信，提醒过期时间
         *  "【嚯哈借阅伴侣】您预订的#book#图书，已预订成功！请您在#time#前确认您的预订，否则图书将不再为您保留。预订单号：#order#";
         * */
        SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.RESERVATION_SUCCESS_TEXT);
        smsHandler.setTelNum(tel);
        smsHandler.setAttribute(BookSetter.BookTag, BookSetter.setBook(book_dao.getBookByIsbn13(order.getISBN()).getTitle()));
        smsHandler.setAttribute(TimeSetter.TimeTag,TimeSetter.setTime(order.getStartTime()));
        smsHandler.setAttribute(OrderSetter.OrderTag,OrderSetter.setOrder(order.getOrderID()));
        smsHandler.send();
    }

    public static void sendBookOutOfTime(ReservationOrder order, String tel){
        /**
         * 发送超时短信
         * "【嚯哈借阅伴侣】您预订的#book#图书，超时未办理预定已失效，图书将不再为您保留，您可以选择重新预定。预订单号：#order#"
         * */
        SMSHandler smsHandler = new SMSHandler(SMSHandlerManager.RESERVATION_FAILED_TEXT);
        smsHandler.setTelNum(tel);
        smsHandler.setAttribute(BookSetter.BookTag, BookSetter.setBook(book_dao.getBookByIsbn13(order.getISBN()).getTitle()));
        smsHandler.setAttribute(OrderSetter.OrderTag,OrderSetter.setOrder(order.getOrderID()));
        smsHandler.send();
    }

    public static Date get3DaysLater(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 3);
        return cal.getTime();
    }

    public static Date get30DaysLater(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 30);
        return cal.getTime();
    }

    public static Date get30MinsLater(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 30);
        return cal.getTime();
    }


    public static String returnAsJson(boolean state, String logInfo, String errMsg,JsonElement element){
        JsonObject object = new JsonObject();
        object.addProperty("state",state);
        object.addProperty("log",logInfo);
        object.addProperty("errMsg",errMsg);
        object.add("content",element);
        return object.toString();
    }

}
