package com.flippey.mychat.utils;

/**
 * @ Author      Flippey
 * @ Creat Time  2016/7/7 20:12
 */
public class StringUtil {

    private static String usernameRegular = "^[a-zA-Z]\\w{2,19}$";
    private static String pwdRegular = "^[0-9]{3,20}$";

    public static boolean validateUsername(String username){

        return username==null?false:username.matches(usernameRegular);

    }

    public static boolean validatePwd(String pwd){
        return pwd==null?false:pwd.matches(pwdRegular);
    }
}
