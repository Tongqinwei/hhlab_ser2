package com.servlet;

import com.beans.book;
import com.beans.comment;
import com.beans.bookpage_bean;
import com.beans.storage_book;
import com.dao.book_dao;
import com.dao.comment_dao;
import com.dao.storage_book_dao;
import com.dao.abstruct_dao;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by hasee on 2017/5/17.
 * 用于获取书籍详细信息
 */
@WebServlet(name = "getbook")
public class getbook extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String isbn13 = request.getParameter("isbn13");
        book Book = book_dao.getBookByIsbn13(isbn13);
        Writer out = response.getWriter();
        if (Book!=null) {
            bookpage_bean Bookpage_bean=Book.toBookpage_bean();
            comment[] comments = comment_dao.getComments(isbn13,1,5);
            Bookpage_bean.setComments(comments);
            Bookpage_bean.setStorage(storage_book_dao.count_transcript(isbn13));
            storage_book[] Storage_books =  storage_book_dao.getStorage_books(isbn13);
            abstruct_dao.close();
            Bookpage_bean.setStorage_books(Storage_books);
            JSONObject book_json= JSONObject.fromObject(Bookpage_bean);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type","text/html;charset=UTF-8");
            response.setContentType("application/json");
            out.write(book_json.toString());
        }else {
            out.write("not exist!");
        }

        out.flush();
        out.close();
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
