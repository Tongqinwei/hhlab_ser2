package com.test;

import com.PriceFetch.PriceFetchException;
import com.PriceFetch.PriceJsonHandler;
import com.PriceFetch.priceHandler;

import java.io.*;

/**
 * Created by lee on 2017/8/21.
 *
 * @author: lee
 * create time: 下午2:26
 */
public class price_fetch {
    public static void main(String args[]) throws IOException, PriceFetchException {
        priceHandler parser = new priceHandler();
        File file = new File("/Users/lee/IdeaProjects/hhlab_ser2/src/com/PriceFetch/example.html");

        InputStreamReader read = new InputStreamReader(
                new FileInputStream(file), "utf-8");// 考虑到编码格式
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        String content = "";

        while ((lineTxt = bufferedReader.readLine()) != null)
        {
            content = content + lineTxt;
        }
        bufferedReader.close();
        read.close();

        parser.parseElement(parser.getElement(content));

        System.out.println(PriceJsonHandler.getErrorJson("123123").toString());
    }
}
