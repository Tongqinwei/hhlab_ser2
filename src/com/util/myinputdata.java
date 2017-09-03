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

//        addbook("A","A1","马克思");
//        addbook("B","B1","哲学");
//        addbook("C","C1","社会科学");
//        addbook("D","D1","法律");
//        addbook("E","E1","军事");
//        addbook("F","F1","经济");
//        addbook("G","G1","教育");
//        addbook("H","H1","语言");
//        addbook("I","I1","文学");
//        addbook("J","J1","历史");
        addbook("A","A2","列宁");
        addbook("A","A3","斯大林");
        addbook("A","A4","毛泽东");
        addbook("A","A5","邓小平");
        addbook("B","B2","中国哲学");
        addbook("B","B3","亚洲哲学");
        addbook("B","B4","非洲哲学");
        addbook("B","B5","欧洲哲学");
        addbook("C","C2","社会团体");
        addbook("C","C3","社会研究");
        addbook("C","C4","社会教育");
        addbook("C","C5","社会丛书");
        addbook("D","D2","中国共产党");
        addbook("D","D3","外国共产党");
        addbook("D","D4","工农组织");
        addbook("D","D5","世界政治");
        addbook("E","E2","中国军事");
        addbook("E","E3","外国军事");
        addbook("F","F2","经济管理");
        addbook("F","F3","农业经济");
        addbook("F","F4","工业经济");
        addbook("F","F5","信息产业经济");
        addbook("G","G2","信息知识传播");
        addbook("G","G3","科学研究");
        addbook("G","G4","教育");
        addbook("G","G5","体育");

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
                comment_dao.add("novas_ghost",Book.getIsbn13(),"good for "+grade,grade,false);
            }
        }
    }
}
