package com.dao;

import com.beans.year_statement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hasee on 2017/8/26.
 */
public class year_statement_dao extends abstruct_dao{
    private static final String sql_init[]={

            "DROP TABLE IF EXISTS year_statement_s; ",

            "Create table year_statement_s ( " +
            "  SELECT " +
            "    name, " +
            "    ordertable.userid AS userid, " +
            "    book.isbn13       AS isbn13, " +
            "    subclass, " +
            "    borrowtime, " +
            "    returntime " +
            "  FROM book_brw, ordertable, user, book, book_mng " +
            "  WHERE book_brw.orderid = ordertable.orderid " +
            "        AND user.userid = ordertable.userid " +
            "        AND book_brw.barcode = book_mng.barcode " +
            "        AND book.isbn13 = book_mng.isbn13 " +
            "        AND orderstate = 4 " +
            "        AND DATE(borrowtime) >= MAKEDATE(EXTRACT(YEAR FROM NOW()),1) " +
            "        AND DATE(returntime) <= DATE(NOW()) " +
            "); ",

            "SET @totusers := " +
                    "( select count(*) from( " +
                    "                        SELECT userid,count(distinct(isbn13)) as books " +
                    "                        FROM year_statement_s " +
                    "                        GROUP BY userid " +
                    "                        ORDER BY books DESC " +
                    "                      ) t1); ",

            "DROP TABLE IF EXISTS year_statement; " ,

            "Create table year_statement ( " +
            "  SELECT " +
            "    name, " +
            "    t1.userid AS userid, " +
            "    t1.books, " +
            "    t2.subclass, " +
            "    t3.defeat " +
            "  FROM " +
            "    (SELECT " +
            "       userid, " +
            "       count(DISTINCT (isbn13)) AS books  " +
            "     FROM year_statement_s " +
            "     GROUP BY userid " +
            "     ORDER BY books DESC) t1, " +
            "    (SELECT " +
            "       userid, " +
            "       subclass  " +
            "     FROM ( " +
            "            SELECT " +
            "              userid, " +
            "              subclass, " +
            "              count(DISTINCT (isbn13)) " +
            "            FROM year_statement_s " +
            "            GROUP BY userid, subclass " +
            "            ORDER BY count(DISTINCT (isbn13)) DESC " +
            "          ) r " +
            "     GROUP BY userid) t2, " +
            "    (SELECT " +
            "       name, " +
            "       userid, " +
            "       (@totusers - (@rowno := @rowno + 1)) / @totusers AS defeat " +
            "     FROM ( " +
            "            SELECT " +
            "              name, " +
            "              userid, " +
            "              count(DISTINCT (isbn13)) AS books " +
            "            FROM year_statement_s " +
            "            GROUP BY userid " +
            "            ORDER BY books DESC " +
            "          ) r, (SELECT @rowno := 0) t) t3 " +
            "  WHERE t1.userid = t2.userid AND t1.userid = t3.userid " +
            ");"};
    public static void init(){
        for (String aSql_init : sql_init) runSQL(aSql_init);
    }
    public static year_statement getYearStatementByUserid(int userid){
        year_statement ans= null;
        abstruct_dao.connect();
        try {
            String sql = String.format("select * from %s where userid = ? ;",table_year_statement);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ans= new year_statement();
                ans.setName(rs.getString("name"));
                ans.setBooks(rs.getInt("books"));
                ans.setDefeat(rs.getDouble("defeat"));
                ans.setSubclass(rs.getString("subclass"));
                ans.setUserid(userid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return ans;
    }
}
