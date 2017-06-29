package com.servlet;

import com.Login.Handler.KeyManager;
import com.Login.Handler.MyJsonParser;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午10:15
 */
@WebServlet(name = "getPublicKeyServlet")
public class getPublicKeyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        String retString = "";
        try {
            KeyManager manager = KeyManager.getInstance();
            Properties properties = manager.getKey();
            retString = MyJsonParser.SetUserInfoModifyResult(true, properties.getProperty("publicKey"));
        } catch (Exception e){
            retString = MyJsonParser.SetUserInfoModifyResult(true, "error when get the public key");
        }

        Writer out = response.getWriter();
        out.write(retString);
        out.close();
        response.flushBuffer();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
