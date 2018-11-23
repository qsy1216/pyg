package com.itheima.ssm.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderUtils {
//
//    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//    public static String encoder(String password){
//        String encode = bCryptPasswordEncoder.encode(password);
//        return encode;
//    }

    public static void main(String[] args) {
        //String password = "123";
       // String encoder = new BCryptPasswordEncoder().encode("123");
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }


}
