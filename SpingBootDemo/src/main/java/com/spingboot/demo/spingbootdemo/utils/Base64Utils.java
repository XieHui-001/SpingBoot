package com.spingboot.demo.spingbootdemo.utils;

import java.util.Base64;

public class Base64Utils {

    public static String endCode(String value){
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
}
