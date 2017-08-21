package com.PriceFetch;

import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/8/21.
 *
 * @author: lee
 * create time: 下午2:10
 */
public class priceHandler {

    public Element getElement(String html) throws PriceFetchException {
        Document doc = Jsoup.parse(html);
        Element div = doc.getElementsByClass("divright").get(0);
        Element tableBody = div.child(1).child(0).child(0);
        Element td = tableBody.child(0).child(0).child(1);
        if (td.tagName() == "td") {
            System.out.println("get element right! td success");
            return td;
        } else {
            System.out.println("get element error!");
            throw new PriceFetchException("Element get errror: \n " + td.toString());
        }
    }

    private void getPriceInfo(Element element, PriceJsonHandler jsonHandler) {
//        System.out.println(element.toString());
        Elements sites = element.getElementsByClass("sitediv");
        Elements prices = element.getElementsByClass("pricediv");
        Elements urls = element.getElementsByClass("godiv");

        for (int i = 0; i < sites.size(); i++) {
            jsonHandler.setSiteInfo(sites.get(i).text().replace("：",""), prices.get(i).text(), getBuyUrl(urls.get(i).child(0)));
            System.out.println(sites.get(i).text() + "  " + prices.get(i).text() + "  " + getBuyUrl(urls.get(i).child(0)));
        }
    }

    private String getBuyUrl(Element element) {

        Map map = URLRequest(element.attr("href"));
        if(map.get("tourl") == null){
            return "";
        } else {
            return map.get("tourl").toString();
        }
    }

    private boolean getResult(Element element, PriceJsonHandler handler){

        if(element.text().contentEquals("作者： 出版社： 出版时间：")){
            System.out.println("Not found!");

            handler.setResult(false,"Not Found");
            return false;

        } else {
            handler.setResult(true,"success");
            return true;

        }
    }

    public JsonObject parseElement(Element element) {

        PriceJsonHandler jsonHandler = new PriceJsonHandler();

        if(!getResult(element.child(1).child(0),jsonHandler)){
            return jsonHandler.getPriceJson();
        }

        getPriceInfo(element.child(1).child(1).child(0), jsonHandler);
        return jsonHandler.getPriceJson();
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;
        strURL = strURL.trim().toLowerCase();
        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }
        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map URLRequest(String URL) {
        Map mapRequest = new HashMap();
        String[] arrSplit = null;
        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
//每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
//解析出键值
            if (arrSplitEqual.length > 1) {
//正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
//只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }





}
