package com.dmg.lobbyserver.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.Charsets;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:05 2019/12/6
 **/
@Slf4j
public class AesUtil {

    public static boolean initialized = false;

    private static final String AES_NAME = "AES";

    private static final String PKCS5P = "AES/CBC/PKCS5Padding";

    private static final String PKCS7P = "AES/CBC/PKCS7Padding";

    /**
     * @Author liubo
     * @Description //TODO 加密
     * @Date 10:51 2019/12/9
     **/
    public static byte[] encrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
        initialize();
        Cipher cipher = Cipher.getInstance(PKCS7P);
        Key sKeySpec = new SecretKeySpec(keyByte, AES_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
        byte[] result = cipher.doFinal(content);
        return result;
    }

    /**
     * @Author liubo
     * @Description //TODO 解密
     * @Date 10:51 2019/12/9
     **/
    public static byte[] decrypt(byte[] content, byte[] keyByte, byte[] ivByte) throws Exception {
        initialize();
        Cipher cipher = Cipher.getInstance(PKCS7P);
        Key sKeySpec = new SecretKeySpec(keyByte, AES_NAME);
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(ivByte));// 初始化
        byte[] result = cipher.doFinal(content);
        return result;
    }

    public static void initialize() {
        if (initialized)
            return;
        Security.addProvider(new BouncyCastleProvider());
        initialized = true;
    }

    // 生成iv
    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance(AES_NAME);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    public static byte[] encrypt(String key, String initVector, byte[] value) throws Exception {
        //指定算法，模式，填充方式，创建一个Cipher
        Cipher cipher = Cipher.getInstance(PKCS5P);
        //生成Key对象
        Key sKeySpec = new SecretKeySpec(key.getBytes(Charsets.UTF_8.name()), AES_NAME);
        //把向量初始化到算法参数
        AlgorithmParameters params = AlgorithmParameters.getInstance(AES_NAME);
        params.init(new IvParameterSpec(initVector.getBytes(Charsets.UTF_8.name())));

        //指定用途，密钥，参数 初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, params);
        return cipher.doFinal(value);
    }

    public static String decrypt(String key, String initVector, byte[] encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(Charsets.UTF_8.name()));
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(Charsets.UTF_8.name()), AES_NAME);

        Cipher cipher = Cipher.getInstance(PKCS5P);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(encrypted);

        return new String(original);
    }


}
