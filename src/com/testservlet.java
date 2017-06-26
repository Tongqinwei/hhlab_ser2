package com;

import com.dao.abstruct_dao;
import com.dao.book_brw_dao;
import com.dao.user_dao;

/**
 * Created by hasee on 2017/6/26.
 */
public class testservlet {
    private static void boReBook(){
        abstruct_dao.connect();
        String operation = "return";
        String barcode = "9787020049295001";
        String unionid = "novas_ghost";

        //book[] books= book_dao.search(key);
        //JSONArray book_json= JSONArray.fromObject(books);
        // out.write(book_json.toString());

        int userid = user_dao.getUserByUnionId(unionid).getUserid();
        String orderid="123";
        String res="falure";
        switch (operation) {
            case "borrow":
                if (book_brw_dao.borrowBook(unionid,orderid,barcode))
                    res="success1";
                break;
            case "return":
                if (book_brw_dao.returnBook(orderid,barcode))
                    res="success2";
                break;
        }
        System.out.println(res);
    }
    public static void main(String argc[]){
        boReBook();
    }
}
