package com.dao;

import com.beans.*;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hasee on 2017/5/19.
 */
public class comment_dao extends abstruct_dao{
    private comment Comment;
    public comment_dao(String user_name,String isbn13,String Comment,double grade){
        super();
        this.Comment = new comment();
        this.Comment.setUser_name(user_name);
        this.Comment.setIsbn13(isbn13);
        this.Comment.setContent(Comment);
        this.Comment.setRate(grade);
    }

    public comment_dao(comment Comment){
        super();
        this.Comment = Comment;
    }

    public boolean add(boolean isWork){
        /*
        * 在comment中加入评论
        * */
        if (!book_dao.isExistByIsbn13(Comment.getIsbn13())){
            System.err.println("The book \"" + Comment.getIsbn13() + "\" is not existed!");
            return false;
        }
        int userid=user_dao.isExistByUnionid(Comment.getUser_name());
        if (userid==-1) {
            System.err.println("The user \""+Comment.getUser_name()+"\" is not existed!");
            return false;
        }
        boolean success=false;
        Comment.setUserid(userid);
        try {
            if (isWork) abstruct_dao.work_begin();
            String sql = String.format("insert into %s(userid , isbn13 , comment , grade )values (?,?,?,?);", table_comment);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, Comment.getUserid());
            ps.setString(2, Comment.getIsbn13());
            ps.setString(3, Comment.getContent());
            ps.setDouble(4, Comment.getRate());
            ps.execute();
            if ( Comment.getRate()>=1) book_dao.addNewGrade(Comment.getIsbn13(),Comment.getRate(),isWork);
            if (isWork) abstruct_dao.work_commit();
            success = true;
        } catch (SQLException e) {
            if (isWork) abstruct_dao.work_rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            return success;
        }
    }

    public int getId(){
        /*
        * 返回userid 失败返回-1
        * */
        try {
            String sql = String.format("select id from %s where userid = ? and isbn13 = ?;", table_comment);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,Comment.getUserid());
            ps.setString(2,Comment.getIsbn13());
            ResultSet rs = ps.executeQuery();
            rs.last();
            if (rs.getRow()==0) return -1;
            else return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isExist(){
        if (this.getId()==-1) return false;
        else return true;
    }

    public comment[] getComments(int _begin, int _end){
        /*
        * 返回userid 失败返回-1
        * */
        try {
            String sql = String.format("select * from %s where isbn13 = ? limit ?,? ;", table_comment);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,Comment.getIsbn13());
            ps.setInt(2,_begin-1);
            ps.setInt(3,_end-1);
            ResultSet rs = ps.executeQuery();
            List<comment> comments= new ArrayList<comment>();
            comments.clear();
            while (rs.next()){
                comment newcomment = new comment();
                newcomment.setId(rs.getInt("id"));
                newcomment.setIsbn13(rs.getString("isbn13"));
                newcomment.setUserid(rs.getInt("userid"));
                newcomment.setContent(rs.getString("comment"));
                newcomment.setRate(rs.getInt("grade"));

                comments.add(newcomment);
            }
            comment[] array =new comment[comments.size()];
            return comments.toArray(array);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static comment[] getComments(String isbn13,int _begin,int _end){
        comment Comment=new comment();
        Comment.setIsbn13(isbn13);
        comment_dao Comment_dao = new comment_dao(Comment);
        return Comment_dao.getComments(_begin,_end);
    }

    public static boolean add(comment Comment,boolean isWork){
        comment_dao Comment_dao = new comment_dao(Comment);
        return Comment_dao.add(isWork);
    }
    public static boolean add(String user_name,String isbn13,String Comment,double grade,boolean isWork){
        comment_dao Comment_dao = new comment_dao(user_name,isbn13,Comment,grade);
        return Comment_dao.add(isWork);
    }
}
