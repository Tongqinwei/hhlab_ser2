package com;
import com.Http.getFromHttp;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.beans.*;
import com.dao.*;

import java.util.List;
import java.util.Vector;

/**
 * Created by hasee on 2017/5/4.
 */
public class getbook {

    public static void  main(String argc[]){
        abstruct_dao.connect();
        //加入用户
        String JSON_tong="{" +
                "\"userid\": 1 ," +
                "\"tel\":\"18210189279\"," +
                "\"unionid\":\"novas_ghost\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1996-05-22\"," +
                "\"email\":\"tongqinw@163.com\"," +
                "\"address\":\"3#_114\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"仝秦玮\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"150203199605222137\"" +
                "}";

        JSONObject sr_user= JSONObject.fromObject(JSON_tong);
        user Tong = (user) JSONObject.toBean(sr_user, user.class);

        //从豆瓣获取书籍
        String url="https://api.douban.com/v2/book/search";
        String param="tag=文学";
        String Jsonstring = getFromHttp.sendGet(url, param);
        System.out.println("GET:"+Jsonstring);

        JSONObject js= JSONObject.fromObject(Jsonstring);
        searchresults sr = (searchresults) JSONObject.toBean(js, searchresults.class);

        //加入书籍
        user_dao.add(Tong);
        book[] Books=sr.getBooks();
        for (int i=0;i<Books.length;i++) {
            book Book = Books[i];
            book_dao testbookdao = new book_dao(Book);
            testbookdao.add_book("A", "A1");
            storage_book_dao.add(Book,"国家图书馆");
            storage_book_dao.add(Book,"北京市图书馆");
            storage_book_dao.add(Book,"北京理工大学图书馆");
            comment_dao.add("novas_ghost",Book.getIsbn13(),"good",5,false);
            comment_dao.add("novas_ghost",Book.getIsbn13(),"not bad",3,false);
            comment_dao.add("novas_ghost",Book.getIsbn13(),"bad",1,false);
        }
        //book[] Books = book_dao.getRecommendBook_index(1,5);

        //JSONArray book_json = JSONArray.fromObject(Books);
        //System.out.println(book_json.toString());

        for (int i=0;i<Books.length-5;i++) {
            book Book = Books[i];
            cart_dao.add(Book.getIsbn13()+"001","novas_ghost",true);
        }
        //cart_dao.rid("9787536023918",1);
        //abstruct_dao.close();
    }
}
