package com.spingboot.demo.spingbootdemo.utils;

import java.util.Base64;

public class Base64Utils {

    /**
     * Base 64 加密
     * @param value
     * @return
     */
    public static String encrypt(String value){
        if (value == null || value.isEmpty()){
            return "";
        }

        String decodedString;
        try{
            byte[] decodedBytes = Base64.getDecoder().decode(value);
            decodedString = new String(decodedBytes);
        }catch (Exception exception){
            throw new RuntimeException("base64编码解析失败");
        }
        return decodedString;
    }

    /**
     * Base 64 解密
     * @param originalString
     * @return
     */
    public static String decrypt(String originalString) {
        byte[] encodedBytes = Base64.getEncoder().encode(originalString.getBytes());
        return new String(encodedBytes);
    }
}
