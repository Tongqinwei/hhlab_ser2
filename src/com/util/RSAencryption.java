package com.util;

import java.security.*;
import java.security.interfaces.*;
import javax.crypto.*;

/**
 * Created by hasee on 2017/6/11.
 */
public class RSAencryption {
    protected static RSAPrivateKey privateKey;
    protected static RSAPublicKey publicKey;
    protected static byte[] resultBytes;

    public RSAencryption(){
        try{

            //生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

            //初始化密钥对生成器，密钥大小为1024位
            keyPairGen.initialize(1024);

            //生成一个密钥对，保存在keyPair中
            KeyPair keyPair = keyPairGen.generateKeyPair();

            //得到私钥和公钥
            privateKey =(RSAPrivateKey) keyPair.getPrivate();
            publicKey = (RSAPublicKey)keyPair.getPublic();

        }catch(Exception e){
            e.printStackTrace();
        }
//        return null;
    }

    public static byte[] encrypt(RSAPublicKey publicKey,byte[] srcBytes){
        if(publicKey != null){
            try{
                //Cipher负责完成加密或解密工作，基于RSA
                Cipher cipher = Cipher.getInstance("RSA");

                //根据公钥，对Cipher对象进行初始化
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);

                //加密，结果保存进resultBytes，并返回
                byte[] resultBytes = cipher.doFinal(srcBytes);
                return resultBytes;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String encrypt(String src){
        //用公钥加密
        byte[] srcBytes = src.getBytes();
        resultBytes = RSAencryption.encrypt(publicKey, srcBytes);
        String result = new String(resultBytes);
        //System.out.println("用公钥加密后密文是:" + result);
        return result;
    }

    public static byte[] decrypt(RSAPrivateKey privateKey,byte[] encBytes){
        if(privateKey != null){
            try{
                Cipher cipher = Cipher.getInstance("RSA");

                //根据私钥对Cipher对象进行初始化
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                //解密并将结果保存进resultBytes
                byte[] decBytes = cipher.doFinal(encBytes);
                return decBytes;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String decrypt(String src){
        if (privateKey==null ||resultBytes==null) return null;
        byte[] decBytes = RSAencryption.decrypt(privateKey, resultBytes);

        return new String(decBytes);
    }

    public static void main(String[] args){
        RSAencryption test = new RSAencryption();

        String liyu="游戏引擎课上利用游戏引擎开发游戏";
        String miwen=RSAencryption.encrypt(liyu);
        String jiemi=RSAencryption.decrypt(miwen);
        System.out.println(liyu);
        System.out.println(miwen);
        System.out.println(jiemi);
    }
}
