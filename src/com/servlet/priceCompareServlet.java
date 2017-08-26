package com.servlet;

import com.Login.Handler.MyJsonParser;
import com.PriceFetch.PriceJsonHandler;
import com.PriceFetch.priceHandler;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by lee on 2017/8/21.
 *
 * @author: lee
 * create time: 下午4:12
 */
public class priceCompareServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = null;
        String isbn = null;

        JsonObject retJson = null;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","text/html;charset=UTF-8");
        response.setContentType("application/json");
        Writer out = response.getWriter();

        // try to get the json and the session user
        try {
            String data = CreateSessionServlet.getBody(request);
            log(" receive from data:" + data);
            jsonObject = MyJsonParser.String2Json(data);
            isbn = jsonObject.get("isbn").getAsString();
        } catch (Exception e) {
            out.write(PriceJsonHandler.getErrorJson("error json").toString());
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            return;
        }

        priceHandler handler = new priceHandler();
        String result = "";
        try {
            result = Jsoup.connect("http://book.manmanbuy.com/Search.aspx?key=" + isbn).get().toString();
//            System.out.println(result);


        } catch (Exception e) {
            out.write(PriceJsonHandler.getErrorJson("http failure").toString());
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            return;
        }

        Element element = null;
        try {
            element = handler.getElement(result);
        } catch (Exception e) {
            e.printStackTrace();
            out.write(PriceJsonHandler.getErrorJson("error parse html").toString());
            out.flush();
            out.close();
            response.flushBuffer();
            return;
        }

        try {
            retJson = handler.parseElement(element);
        } catch (Exception e) {
            out.write(PriceJsonHandler.getErrorJson("error when read price").toString());
            out.flush();
            out.close();
            response.flushBuffer();
            e.printStackTrace();
            return;
        }

        out.write(retJson.toString());
        out.flush();
        out.close();
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(404);
    }
}


