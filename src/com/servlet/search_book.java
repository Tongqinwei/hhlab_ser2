package com.servlet;

import com.beans.book;
import com.beans.user;
import com.dao.abstruct_dao;
import com.dao.book_dao;
import com.dao.user_dao;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/17.
 * 用于书籍搜索
 */
@WebServlet(name = "search_book")
public class search_book extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String key = request.getParameter("key");
        int beginindex=1;
        int endindex=1;
        String mode ="0";
        if (request.getParameter("beginindex")!=null){
            beginindex=Integer.parseInt( request.getParameter("beginindex"));
        }
        if (request.getParameter("mode")!=null){
            mode=request.getParameter("mode");
        }
        if (beginindex>=endindex) endindex=beginindex+20;


        abstruct_dao.connect();
        JSONArray book_json=null;
        JSONArray user_json=null;
        if (mode.equals("0")){
            book[] books= book_dao.search(key,beginindex,endindex);
            book_json= JSONArray.fromObject(books);
            user[] Users= user_dao.searchUser(key,beginindex,endindex);
            user_json= JSONArray.fromObject(Users);
        }else if (mode.equals("1")){
            book[] books= book_dao.searchBySubclass(key,beginindex,endindex);
            book_json= JSONArray.fromObject(books);
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out1 = response.getWriter();
        assert book_json != null;
        if (user_json!=null){
            out1.write("{\"books\":"+book_json.toString()+",\"users\":"+user_json.toString()+"}");
        }else if (mode.equals("0")) {
            out1.write("{\"books\":"+book_json.toString()+",\"users\":[]}");
        }else{
            out1.write(book_json.toString());
        }
        out1.flush();
        out1.close();
        response.flushBuffer();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
