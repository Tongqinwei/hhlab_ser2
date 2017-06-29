package com.Login.Handler;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.*;
import java.util.Date;
import java.util.Properties;

/**
 * Created by lee on 2017/6/29.
 *
 * @author: lee
 * create time: 下午8:45
 */
public class rsaManager {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String filePath = "";

    public rsaManager(){
        privateKey = null;
        publicKey = null;
        filePath = Thread.currentThread().getContextClassLoader().getResource("com/util/rsa.properties").getFile();
    }

    private void generateKeyPair(){
        KeyPairGenerator keyPairGenerator = null;
        KeyPair pair = null;
        try {
            //生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

            //初始化密钥对生成器，密钥大小为1024位
            keyPairGen.initialize(1024);

            //生成一个密钥对，保存在keyPair中
            pair = keyPairGen.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
    }

    public void updateKey(){
        generateKeyPair();

        Properties properties = new Properties();
        properties.setProperty("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
        properties.setProperty("privateKey",Base64.encodeBase64String(privateKey.getEncoded()));
        try {
            OutputStream outputStream = new FileOutputStream(filePath);
            properties.store(outputStream, "update in "+new Date().getTime());
            outputStream.close();
        } catch (IOException a) {
            a.printStackTrace();
        }

    }

    public Properties getKey(){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(filePath);
            properties.load(inputStream);
            inputStream.close(); //关闭流
        } catch (IOException a) {
            a.printStackTrace();
        }
        return properties;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}