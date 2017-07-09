package com.Login.Handler;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import static java.awt.SystemColor.text;

/**
 * Created by lee on 2017/6/30.
 *
 * @author: lee
 * create time: 上午6:40
 */
public class Decrpter {
    public static void main(String args[]) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, BadPaddingException, IllegalBlockSizeException {
        BigInteger modulus = new BigInteger("BAE41604E1F3C8675BDD1D938680B7A8D30E5A1C6223577A6B1D537558EFE3A08874EABCC29B2D3841A44984C3FBD23E356A3887F4DD15265A420C59F1CC4083275942761A62167BBDF25D32D607BF4E2C2646DE92D8FD1DFC9C91177E0F5E35BACD5EC155BFE13BA5A7596D53220E39ECF647CA8FA93B15B8A3E6DDDA64384D", 16);
        BigInteger pubExp = new BigInteger("010001", 16);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        String target = "bY7mTFgfa1+zSijSh8aLvX2wUR55wvda+uUmPpYzDtBZvc3yJlQVBdS5eqbY2DOHweSq5oPgdjRisDS1DT0LGQXW3hEduddORglJTYFaEi/FC9O3vfPEikeZXUYI+IQispM1WcttdOENltX7to0Vt/rc4sW2cYd0vmluRjFkxJE=";

        byte[] cipherData = cipher.doFinal(Base64.decodeBase64(target.getBytes()));
        System.out.println(new String(cipherData));
    }
}
