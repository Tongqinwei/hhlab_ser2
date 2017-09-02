package com.test;

import com.PriceFetch.PriceFetchException;
import com.PriceFetch.PriceJsonHandler;
import com.PriceFetch.priceHandler;
import com.dao.UserImageDao;

import java.io.*;

/**
 * Created by lee on 2017/8/21.
 *
 * @author: lee
 * create time: 下午2:26
 */
public class price_fetch {
    public static void main(String args[]) throws IOException, PriceFetchException {
        try {
            UserImageDao.UpdateUserImage("oycMK0cowCKFt77hYwYTSvV5GmTg","123213");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
}
