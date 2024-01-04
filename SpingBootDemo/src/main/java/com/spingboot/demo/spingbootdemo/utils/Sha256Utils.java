package com.spingboot.demo.spingbootdemo.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class Sha256Utils {
    public static String coding(String value){

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 将输入字符串转换为字节数组
        byte[] encodedHash = digest.digest(value.getBytes(StandardCharsets.UTF_8));

        // 将字节数组转换为十六进制字符串
        StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
        for (byte b : encodedHash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }

        log.info("服务端加密数据:"+hexString);

        return hexString.toString();
    }

    public static Boolean checkLogin(String serviceValue,String clientValue){
        return clientValue.equalsIgnoreCase(coding(serviceValue));
    }
}
