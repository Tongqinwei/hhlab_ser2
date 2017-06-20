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
        String url="https://api.douban.com/v2/book/search";
        String param="tag=文学";
        //发送 GET 请求
//        String Jsonstring="{\"rating\":{\"max\":10,\"numRaters\":19,\"average\":\"9.0\",\"min\":0}," +
//                "\"author\":[\"[美] 布鲁克·诺埃尔·穆尔\",\"肯尼思·布鲁德\"]," +
//                "\"image\":\"https://img3.doubanio.com/mpic/s29286815.jpg\"," +
//                "\"bookId\":\"1237\"," +
//                "\"publisher\":\"后浪丨北京联合出版公司\"," +
//                "\"isbn10\":\"7544826449\"," +
//                "\"isbn13\":\"9787544826440\"," +
//                "\"title\":\"思想的力量\"," +
//                "\"guide_read\":\"纵览西方古今哲学流派\\n关注思想蕴含的内在力量\\n呈现哲学与现实的互动关系\\n………………" +
//                "\\n※学者推荐※\\n《思想的力量》是部精心构思的佳作，在思想介绍和原著选读之间做到了极佳的平衡，能够很好地引导学生深入学习。全书融合" +
//                "了以历史为导向和以问题为导向的两种进路，使之足以灵活适用于大多数的哲学导论课程。\\n——詹姆斯&#183克雷格&#183汉克斯（James Craig Hanks），" +
//                "美国德克萨斯州立大学教授\\n我认为穆尔和布鲁德的《思想的力量》是部非凡的杰作。在这部著作中，作者成功地将历史语境和哲学思想相互融合，" +
//                "最终让学生乐于阅读，老师热衷使用。\\n——克里斯塔&#183林恩&#183亚当斯（Christa Lynn Adams），美国雷克兰社区学院教授\\n………………\\n" +
//                "※内容简介※\\n《思想的力量(第9版)》是迄今为止覆盖面最全的哲学史读本，内容包罗万象，纵览了西方从古至今的所有哲学理论流派。全书写作风格生动，" +
//                "让哲学变得通俗易懂，却又不至于过分简化，每章章末还附有重要思想家的原著选读。作者着重展现了哲学思想如何影响人们的现实生活，考察了哲学思想与人类历史、" +
//                "日常生活的互动关系。本书作为美国大学经典哲学教材，至今已出到第9版，广受各类读者欢迎。\"," +
//                "\"comments\":[" +
//                "{\"id\":\"1234\",\"user_name\":\"novas_ghost\",\"rate\":5,\"content\":\"wahhhhh,不错哦\"}," +
//                "{\"id\":\"1235\",\"user_name\":\"novas_ghost\",\"rate\":1,\"content\":\"wahhhhh,不错哦\"}," +
//                "{\"id\":\"1236\",\"user_name\":\"novas_ghost\",\"rate\":3,\"content\":\"wahh,不错\"}," +
//                "{\"id\":\"1237\",\"user_name\":\"novas_ghost\",\"rate\":2,\"content\":\"w,不好看\"}," +
//                "{\"id\":\"1238\",\"user_name\":\"novas_ghost\",\"rate\":4,\"content\":\"不错哦\"}" +
//                "]," +
//                "\"storage\":5," +
//                "\"storage_books\":[" +
//                "{\"book_id\":\"1231415\",\"book_state\":\"available\",\"book_location\":\"良乡社会科学图书第三阅览室\"}," +
//                "{\"book_id\":\"1231416\",\"book_state\":\"available\",\"book_location\":\"良乡社会科学图书第三阅览室\"}," +
//                "{\"book_id\":\"1231417\",\"book_state\":\"available\",\"book_location\":\"良乡社会科学图书第三阅览室\"}," +
//                "{\"book_id\":\"1231418\",\"book_state\":\"borrowed\",\"return_time\":\"2017年4月19日\",\"book_location\":\"良乡社会科学图书第三阅览室\"}," +
//                "{\"book_id\":\"1231419\",\"book_state\":\"available\",\"return_time\":\"2017年4月20日\",\"book_location\":\"良乡社会科学图书第三阅览室\"}" +
//                "]" +
//                "}";
        String Jsonstring = getFromHttp.sendGet(url, param);
        System.out.println("GET:"+Jsonstring);

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

        JSONObject js= JSONObject.fromObject(Jsonstring);
        searchresults sr = (searchresults) JSONObject.toBean(js, searchresults.class);

        JSONObject sr_user= JSONObject.fromObject(JSON_tong);
        user Tong = (user) JSONObject.toBean(sr_user, user.class);

        user_dao.add(Tong);
        book[] Books=sr.getBooks();
        for (int i=0;i<Books.length;i++) {
            book Book = Books[i];
            book_dao testbookdao = new book_dao(Book);
            testbookdao.add_book("A", "A1");
            storage_book_dao.add(Book,"国家图书馆");
            storage_book_dao.add(Book,"北京市图书馆");
            storage_book_dao.add(Book,"北京理工大学图书馆");
            comment_dao.add("novas_ghost",Book.getIsbn13(),"good",5);
            comment_dao.add("novas_ghost",Book.getIsbn13(),"not bad",3);
            comment_dao.add("novas_ghost",Book.getIsbn13(),"bad",1);
        }
//        int i=0;
//        abstruct_dao.close();
        //book[] Books = book_dao.getRecommendBook_index(1,5);
        abstruct_dao.close();
        JSONArray book_json = JSONArray.fromObject(Books);
        System.out.println(book_json.toString());
    }
}
