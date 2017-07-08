package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Created by hasee on 2017/7/6.
 */
public class testCF_JDBC {
    static Map<Long, Long> hashMap = new HashMap<Long, Long>();

    //返回一个<userID,itemID>的hashMap，意义是为User推荐对应的item。
    public HashMap<Long, Long> SVDlist(DataModel model,
                                       ArrayList<Long> userIDs, ArrayList<Long> brandIDs)
            throws TasteException {
        System.out.println("-------------------------------------------------------- ");
        //这里有另外一种协同过滤算法，基于Item的，我测试结果不是很好，就没用了。
        // ItemSimilarity similarity = new PearsonCorrelationSimilarity(
        // model);
        // Recommender recommender = new GenericItemBasedRecommender(model,
        // similarity);
        Recommender recommender = new SVDRecommender(model,
                new ALSWRFactorizer(model, 10, 0.75, 20));

        for (int i = 0; i < userIDs.size(); i++) {
            float score = 0, scoretemp = 0;
            Long bestBrand = 0L;

            for (int j = 0; j < brandIDs.size(); j++) {
                scoretemp = recommender.estimatePreference(userIDs.get(i), brandIDs.get(j));
                System.out.print(" "+scoretemp);
                if (scoretemp > score) {
                    score = scoretemp;
                    bestBrand = brandIDs.get(j);
                }
                hashMap.put(userIDs.get(i), bestBrand);
            }
            System.out.println("The best brand for " + userIDs.get(i) + " is "
                    + bestBrand);
        }
        return (HashMap<Long, Long>) hashMap;
    }

    public static void main(String[] args) throws Exception {

        String driver = "com.mysql.jdbc.Driver";
        String host = "127.0.0.1";
        String user = "remote_user";
        String password = "remote";
        String databasename = "hhlab";

        Class.forName(driver);
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(databasename);

        JDBCDataModel jdbcDataModel = new MySQLJDBCDataModel(dataSource,
                "ubhvor", "user_id", "item_id", "preference", null);
        //利用ReloadFromJDBCDataModel包裹jdbcDataModel,可以把输入加入内存计算，加快计算速度。
        ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(
                jdbcDataModel);
        //这里的refresh是刷新model，一般情况下我觉得用不上，因为像我这个程序，每次都会新建立datamodel，都是最新的，所以不用刷新
        //当然，如果你需要在内存中存储下model，然后自己的taste_preferences时刻在变化，此时是需要刷新的。
        //model.refresh(null);

        //测试的brandIds可以从自己的数据库中获得，这里为了简单，只是加了几个做测试。
        ArrayList<Long> userIDs = new ArrayList<Long>();
        userIDs.add(1L);
        userIDs.add(2L);
        userIDs.add(3L);

        ArrayList<Long> brandIDs = new ArrayList<Long>();
        brandIDs.add(9787020002207L);
        //brandIDs.add(9787020042494L);
        //brandIDs.add(9787208061644L);
        //brandIDs.add(9787506261579L);
        brandIDs.add(9787512500983L);
        brandIDs.add(9787532731077L);
        brandIDs.add(9787532736874L);
        brandIDs.add(9787020024759L);
        brandIDs.add(9787532725694L);
        //brandIDs.add(9787020049295L);
        testCF_JDBC test = new testCF_JDBC();
        hashMap = test.SVDlist(model, userIDs, brandIDs);
        //在之后你可以把这数据写入自己的数据库，或者直接post给他前端，让前端显示相应的产品给用户。
        int i0=0;
    }
}
