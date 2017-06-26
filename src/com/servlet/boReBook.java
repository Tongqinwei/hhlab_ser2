package com.servlet;

import com.dao.abstruct_dao;
import com.dao.book_brw_dao;
import com.dao.user_dao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by hasee on 2017/6/26.
 */
@WebServlet(name = "cartHandler")
public class boReBook extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
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
        String operation = request.getParameter("operation");//borrow,return
        String barcode = request.getParameter("barcode");
        String unionid = request.getParameter("unionid");

        //book[] books= book_dao.search(key);
        //JSONArray book_json= JSONArray.fromObject(books);
        // out.write(book_json.toString());

        int userid = user_dao.getUserByUnionId(unionid).getUserid();
        String orderid="123";
        switch (operation) {
            case "borrow":
                if (book_brw_dao.borrowBook(unionid,orderid,barcode))
                    retString="success";
                break;
            case "return":
                if (book_brw_dao.returnBook(orderid,barcode))
                    retString="success";
                break;
        }


        out.write(retString);
        //结尾
        out.flush();
        out.close();
        response.flushBuffer();
    }
}
