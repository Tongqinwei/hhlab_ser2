package com.dao;


import java.sql.PreparedStatement;

/**
 * Created by lee on 2017/9/1.
 *
 * @author: lee
 * create time: 下午6:45
 */
public class UserImageDao extends abstruct_dao{
    public UserImageDao(){
        super();
    }

    public static void UpdateUserImage(String unionID, String avatarURL) throws Exception {
        UserImageDao dao = new UserImageDao();
        abstruct_dao.connect();

        try {
            String sql = String.format("UPDATE % SET image =  ? where unionid = ?;", table_user);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, avatarURL);
            ps.setString(2, unionID);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("failed to update");
        }finally {
            abstruct_dao.close();
        }
    }


}
