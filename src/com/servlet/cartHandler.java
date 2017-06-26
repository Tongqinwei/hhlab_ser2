package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.Login.Sessions.SessionManager;
import com.beans.book;
import com.beans.book_brief;
import com.dao.abstruct_dao;
import com.dao.cart_dao;
import com.dao.user_dao;
import com.google.gson.JsonObject;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/22.
 */
@WebServlet(name = "cartHandler")
public class cartHandler extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";


        //取参数
        JsonObject jsonObject = MyJsonParser.String2Json(CreateSessionServlet.getBody(request));
        String unionid = null;
        String operation = null;
        String isbn13 = null;
        String session_id = null;

        try {
            //unionid = jsonObject.get("open_id").getAsString();
            operation = jsonObject.get("operation").getAsString();
            isbn13 = jsonObject.get("isbn13").getAsString();
            session_id = jsonObject.get("session_id").getAsString();
        } catch (Exception e){
            out.write("failure: error json type");
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        unionid = SessionManager.getInstance().getUser(session_id).getOpenID();

        //book[] books= book_dao.search(key);
        //JSONArray book_json= JSONArray.fromObject(books);
       // out.write(book_json.toString());

        int userid = user_dao.getUserByUnionId(unionid).getUserid();
        switch (operation) {
            case "show":
                book_brief[] books= cart_dao.getbooks(userid);
                JSONArray book_brief_json = JSONArray.fromObject(books);
                retString = book_brief_json.toString();
                break;
            case "add":
                if (cart_dao.add(isbn13,userid)) retString="success";
                break;
            case "delete":
                if (cart_dao.rid(isbn13,userid)) retString="success";
                break;
        }


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //开头
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        abstruct_dao.connect();
        Writer out = response.getWriter();
        String retString="failure";

        //取参数
        String operation = request.getParameter("operation");
        String isbn13 = request.getParameter("isbn13");
        String unionid = request.getParameter("unionid");

        //book[] books= book_dao.search(key);
        //JSONArray book_json= JSONArray.fromObject(books);
        // out.write(book_json.toString());

        int userid = user_dao.getUserByUnionId(unionid).getUserid();
        switch (operation) {
            case "show":
                book_brief[] books= cart_dao.getbooks(userid);
                JSONArray book_brief_json = JSONArray.fromObject(books);
                retString = book_brief_json.toString();
                int k;
                break;
            case "add":
                if (cart_dao.add(isbn13,userid)) retString="success";
                break;
            case "delete":
                if (cart_dao.rid(isbn13,userid)) retString="success";
                break;
        }


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
