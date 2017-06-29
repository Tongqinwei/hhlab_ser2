package com.util;

import com.Http.getFromHttp;
import com.beans.book;
import com.beans.searchresults;
import com.beans.user;
import com.dao.*;
import net.sf.json.JSONObject;

/**
 * Created by hasee on 2017/6/27.
 */
public class myinputdata {
    static user[] Users = new user[6];
    static int usertot=0;
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
                "\"name\":\"阿花\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"150203199605222137\"" +
                "}";
        adduser(JSON_tong);
        String JSON_liyu="{" +
                "\"userid\": 2 ," +
                "\"tel\":\"15624952046\"," +
                "\"unionid\":\"liyu233\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1995-12-18\"," +
                "\"email\":\"871858041@qq.com\"," +
                "\"address\":\"3#_113\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"阿发\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"1234123434652342343\"" +
                "}";
        adduser(JSON_liyu);
        String JSON_suchangqing="{" +
                "\"userid\": 3 ," +
                "\"tel\":\"18401669626\"," +
                "\"unionid\":\"S_CQ\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1996-05-28\"," +
                "\"email\":\"2831616471@qq.com\"," +
                "\"address\":\"6#_113\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"阿虎\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"1234123434652342343\"" +
                "}";
        adduser(JSON_suchangqing);
        String JSON_liyu2="{" +
                "\"userid\": 4 ," +
                "\"tel\":\"15624952046\"," +
                "\"unionid\":\"liyu233_2\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1995-12-18\"," +
                "\"email\":\"871858041@qq.com\"," +
                "\"address\":\"3#_113\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"阿哈\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"1234123434652342343\"" +
                "}";
        adduser(JSON_liyu2);
        String JSON_liyu3="{" +
                "\"userid\": 5 ," +
                "\"tel\":\"15624952046\"," +
                "\"unionid\":\"liyu233_3\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1995-12-18\"," +
                "\"email\":\"871858041@qq.com\"," +
                "\"address\":\"3#_113\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"阿呜\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"1234123434652342343\"" +
                "}";
        adduser(JSON_liyu3);
        String JSON_liyu4="{" +
                "\"userid\": 6 ," +
                "\"tel\":\"15624952046\"," +
                "\"unionid\":\"liyu233_4\"," +
                "\"degree\": 4 ," +
                "\"birthday\":\"1995-12-18\"," +
                "\"email\":\"871858041@qq.com\"," +
                "\"address\":\"3#_113\"," +
                "\"postcode\":\"100086\"," +
                "\"name\":\"阿福\"," +
                "\"certificate\":1," +
                "\"certificateid\":\"1234123434652342343\"" +
                "}";
        adduser(JSON_liyu4);

        addbook("A","A1","马克思");
        addbook("B","B1","哲学");
        addbook("C","C1","社会科学");
        addbook("D","D1","法律");
        addbook("E","E1","军事");
        addbook("F","F1","经济");
        addbook("G","G1","教育");
        addbook("H","H1","语言");
        addbook("I","I1","文学");
        addbook("J","J1","历史");
        addbook("K","K1","自然科学");

        abstruct_dao.close();
    }

     private static void adduser(String json){
        JSONObject sr_user= JSONObject.fromObject(json);
        user User = (user) JSONObject.toBean(sr_user, user.class);
        Users[usertot++]=User;
        user_dao.add(User);
    }

    private static void addbook(String _class,String subclass,String tag){
        //从豆瓣获取书籍
        String url="https://api.douban.com/v2/book/search";
        String param="tag="+tag;
        String Jsonstring = getFromHttp.sendGet(url, param);
        System.out.println("GET:"+Jsonstring);

        JSONObject js= JSONObject.fromObject(Jsonstring);
        searchresults sr = (searchresults) JSONObject.toBean(js, searchresults.class);
        java.util.Random r=null;
        r=new java.util.Random();
        //加入书籍
        book[] Books=sr.getBooks();
        for (int i=0;i<Books.length;i++) {
            book Book = Books[i];
            book_dao testbookdao = new book_dao(Book);
            testbookdao.add_book(_class, subclass);
            int storage_sum=r.nextInt(3)+1;
            for (int j=1;j<storage_sum+1;j++){
                storage_book_dao.add(Book,"第"+j+"阅览室");
            }
            int comment_sum=r.nextInt(3)+1;
            for (int j=0;j<comment_sum;j++){
                int userid=r.nextInt(6);
                double grade=r.nextInt(50)/10.0;
                comment_dao.add(Users[userid].getUnionid(),Book.getIsbn13(),"hahaha",grade,false);
            }
        }
    }
}
