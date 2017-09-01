package com.dao;

/**
 * Created by hasee on 2017/5/8.
 */
import com.beans.*;
import com.JDBC.*;
import com.util.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class book_dao extends abstruct_dao{
    private book Book;

    public book_dao(book Book){
        super();
        this.Book=Book;
    }

    public book_dao(Connection conn,book Book){
        super(conn);
        this.Book=Book;
    }

    private ResultSet getResultSetByIsbn13(String isbn13){
        /*
        * 查询isbn13，返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = "select * from "+table_book+" where isbn13=\""+isbn13+"\";";
            ResultSet rs = stat.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResultSet getResultSetByRecommend_index(int _begin,int _end){
        /*
        * 查询isbn13，返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s order by grade_ave desc limit ?,?;",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,_begin-1);
            ps.setInt(2,_end-1);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResultSet getAllbooks(int _begin,int _end){
        /*
        * 查询isbn13，返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s order by subclass asc limit ?,?;",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,_begin-1);
            ps.setInt(2,_end-1);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static book getBookByResultSet(ResultSet rs){
        /*
        * 根据结果集返回book
        * */
        try {
            book _Book = null;
            while(rs.next()) {
                _Book = new book();
                _Book.set_class(rs.getString("class"));
                _Book.setSubclass(rs.getString("subclass"));
                _Book.setImage(rs.getString("cover"));
                _Book.setTitle(rs.getString("title"));
                _Book.setPinyin(rs.getString("pinyin"));
                String[] author=authorutil.string2Authorarr(rs.getString("author"));
                _Book.setAuthor(author);
                _Book.setIsbn13(rs.getString("isbn13"));
                _Book.setIsbn10(rs.getString("isbn10"));
                _Book.setPreface(rs.getString("preface"));
                _Book.setContents(rs.getString("contents"));
                _Book.setPublisher(rs.getString("press"));
                _Book.setVersion(rs.getString("version"));
                _Book.setSummary(rs.getString("introduction"));
                _Book.setStorage(rs.getInt("storage"));
                _Book.setStorage_cb(rs.getInt("storage_cb"));
                _Book.setGrade_times(rs.getInt("grade_times"));
                _Book.setGrade_ave(rs.getDouble("grade_ave"));
                DecimalFormat df = new DecimalFormat("#.0");
                _Book.setGrade_ave_f(df.format(_Book.getGrade_ave()));

            }
            return _Book;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
}

    public static List<book> getBooksByResultSet(ResultSet rs){
        /*
        * 根据结果集返回books 结果为vector
        * */
        try {
            List<book> Books= new Vector();
            while(rs.next()){
                book _Book = new book();
                _Book.set_class(rs.getString("class"));
                _Book.setSubclass(rs.getString("subclass"));
                _Book.setImage(rs.getString("cover"));
                _Book.setTitle(rs.getString("title"));
                _Book.setPinyin(rs.getString("pinyin"));
                String[] author=authorutil.string2Authorarr(rs.getString("author"));
                _Book.setAuthor(author);
                _Book.setIsbn13(rs.getString("isbn13"));
                _Book.setIsbn10(rs.getString("isbn10"));
                _Book.setPreface(rs.getString("preface"));
                _Book.setContents(rs.getString("contents"));
                _Book.setPublisher(rs.getString("press"));
                _Book.setVersion(rs.getString("version"));
                _Book.setSummary(rs.getString("introduction"));
                _Book.setStorage(rs.getInt("storage"));
                _Book.setStorage_cb(rs.getInt("storage_cb"));
                _Book.setGrade_times(rs.getInt("grade_times"));
                _Book.setGrade_ave(rs.getDouble("grade_ave"));
                DecimalFormat df = new DecimalFormat("#.0");
                _Book.setGrade_ave_f(df.format(_Book.getGrade_ave()));
                Books.add(_Book);
            }
            return Books;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExist(){
        /*
        * 判断一本书是否在book中存在
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s where isbn13='%s';", table_book, Book.getIsbn13());
            ResultSet rs = stat.executeQuery(sql);
            rs.last();
            if (rs.getRow()==0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean isExistByIsbn13(String isbn13){
        book BookForIsbn13=new book();
        BookForIsbn13.setIsbn13(isbn13);
        book_dao BookForIsbn13_dao =new book_dao(BookForIsbn13);
        return BookForIsbn13_dao.isExist();
    }

    public boolean add_book(String first_class,String sub_class){
        /*
        * 在book中加入新书，输入为分类和子类
        * */
        if (isExist()) return true;
        boolean success=false;
        try {
            String sql = String.format("insert into %s(class , subclass , cover , title , author , isbn13 , isbn10 " +
                    ",preface , contents , press , version , introduction ,pinyin)" +
                    "values (?,?,?,?,?,?,?,?,?,?,?,?,to_pinyin(?));", table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, first_class);
            ps.setString(2, sub_class);
            ps.setString(3, Book.getImage());
            ps.setString(4, Book.getTitle());

            String authorstring=authorutil.authorarr2String(Book.getAuthor());
            ps.setString(5, authorstring);
            ps.setString(6, Book.getIsbn13());
            ps.setString(7, Book.getIsbn10());
            ps.setString(8, "");
            ps.setString(9, "");
            ps.setString(10,Book.getPublisher());
            ps.setString(11,"");
            ps.setString(12,Book.getSummary());
            ps.setString(13,Book.getTitle());
            ps.execute();
            success=true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    private boolean changeStorageOrS_cb(boolean isStorage,boolean isInc){
        /*
        * 用于有修改库存
        * T T 总库存加
        * T F 总库存减
        * F T 可借库存加
        * F F 可借库存减
        * 暂时只在借书订单中有调用，添加新书新库存不掉用
        * */
        if (!isExist()) return false;
        String attribute="storage";
        if (!isStorage) attribute+="_cb";
        String op="+";
        if (!isInc) op="-";
        boolean success =false;
        try {
            String sql = String.format("update %s set %s=%s%s1 where isbn13 = ?",table_book,attribute,attribute,op);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, Book.getIsbn13());
            ps.execute();
            success =  true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public static boolean incStorage(String isbn13){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.changeStorageOrS_cb(true,true);
    }

    public static boolean decStorage(String isbn13){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.changeStorageOrS_cb(true,false);
    }

    public static boolean incStorage_cb(String isbn13){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.changeStorageOrS_cb(false,true);
    }

    public static boolean decStorage_cb(String isbn13){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.changeStorageOrS_cb(false,false);
    }

    public book[] getRecommendedByIsbn13(int _begin,int _end){
        /*
        * 根据isbn13返回推荐的书目，
        * */
        try {
            String sql = String.format("select * from %s where isbn13 = \"%s\";", table_comment, Book.getIsbn13());
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(sql);
            //ResultSet rs = ps.executeQuery();
            String _class,subclass;
            while (rs.next()){
                _class=rs.getString("class");
                subclass=rs.getString("subclass");
                break;
            }
            sql ="";
            List<book> books= getBooksByResultSet(rs);
            book[] array =new book[books.size()];
            return books.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static boolean add(book Book){
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.add_book(Book.get_class(),Book.getSubclass());
    }

    public static book[] getRecommendedByIsbn13(String isbn13, int _begin,int _end){
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.getRecommendedByIsbn13(_begin,_end);
    }

    public static book getBookByIsbn13(String isbn13){
        book_dao Book_dao= new book_dao(new book());
        return book_dao.getBookByResultSet(Book_dao.getResultSetByIsbn13(isbn13));
    }

    public static book[] getRecommendBook_index(int _begin,int _end){
        book_dao Book_dao= new book_dao(new book());
        List<book> books=getBooksByResultSet(Book_dao.getResultSetByRecommend_index(_begin,_end));
        assert books != null;
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public static book[] getAllBooks(int _begin,int _end){
        book_dao Book_dao= new book_dao(new book());
        List<book> books=getBooksByResultSet(Book_dao.getAllbooks(_begin,_end));
        assert books != null;
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public static book_brief[] getAllBook_briefs(int _begin,int _end){
        book[] Books = getAllBooks(_begin,_end);
        book_brief[] ans = new book_brief[Books.length];
        for (int i=0;i<Books.length;i++){
            ans[i]=Books[i].toBook_brief("");
        }
        return ans;
    }

    private static ResultSet searchToGetResultSet(String key,int _begin,int _end){
        /*
        * 查找书籍，按照isbn13，书名，书名首字母，书名拼音
        * */
        abstruct_dao.connect();
        try {
            String sql=String.format("select distinct * from %s where isbn13=? or title like ? or pinyin like ? limit ?,?",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,key);
            ps.setString(2,"%"+key+"%");
            ps.setString(3,"%"+key+"%");
            ps.setInt(4,_begin-1);
            ps.setInt(5,_end-1);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static ResultSet searchBySubclassToRs(String subclass,int _begin,int _end){
        /*
        * 通过分类查找书籍
        * */
        abstruct_dao.connect();
        try {
            String sql=String.format("select * from %s where subclass=? order by grade_ave limit ?,?",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,subclass);
            ps.setInt(2,_begin-1);
            ps.setInt(3,_end-1);
            ResultSet  rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static book[] search(String key,int _begin,int _end){
        /*
        * 查找书籍，按照isbn13，书名，书名首字母，书名拼音
        * */
        List<book> books = book_dao.getBooksByResultSet(searchToGetResultSet(key, _begin,_end));

        assert books != null;
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public static book[] searchBySubclass(String subclass,int _begin,int _end){
        List<book> books = book_dao.getBooksByResultSet(searchBySubclassToRs(subclass,_begin,_end));

        assert books != null;
        book[] array =new book[books.size()];
        return books.toArray(array);
    }

    public boolean addNewGrade(double grade,boolean isWork){
        /*
        * 添加新分数
        * */
        if (!isExist()) return false;
        boolean success =false;
        try {
            if (isWork) abstruct_dao.work_begin();
            String sql = String.format("update %s set grade_ave=(grade_ave*grade_times+?)/(grade_times+1), grade_times=grade_times+1 where isbn13 = ?",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, grade);
            ps.setString(2, Book.getIsbn13());
            ps.execute();
            if (isWork) abstruct_dao.work_commit();
            success =  true;
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public static boolean addNewGrade(String isbn13,double grade,boolean isWork) {
        book Book=new book();
        Book.setIsbn13(isbn13);
        book_dao Book_dao = new book_dao(Book);
        return Book_dao.addNewGrade(grade,isWork);
    }

    public static void importFile(String OS ,String filename){
        if ("Linux".equals(OS)) abstruct_dao.importFileLinux(table_book,filename);
        else if ("Windows".equals(OS)) abstruct_dao.importFileWindows(table_book,filename);
    }

    public ResultSet searchOfAdmin(int _begin,int _end){
        /*
        * 返回结果集
        * */
        try {
            Statement stat = conn.createStatement();
            String sql = String.format("select * from %s order by isbn13 limit ?,?;",table_book);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,_begin-1);
            ps.setInt(2,_end-1);
            ResultSet rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static book[] searchOfAdmins(int _begin,int _end){
        book_dao Book_dao= new book_dao(new book());
        List<book> books=getBooksByResultSet(Book_dao.searchOfAdmin(_begin,_end));
        assert books != null;
        book[] array =new book[books.size()];
        return books.toArray(array);
    }
}
