package com.flippey.mychat.utils;

import android.text.TextUtils;

import com.hyphenate.util.HanziToPinyin;

import java.util.ArrayList;

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

    public static String getInitial(String name) {
        if (TextUtils.isEmpty(name)) {
            return name;
        }

        //如果是汉字，会自动转换为全大写到拼音
        ArrayList<HanziToPinyin.Token> list =
                HanziToPinyin.getInstance().get(name);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            HanziToPinyin.Token token = list.get(i);
            sb.append(token.target);
        }
        return sb.substring(0,1).toUpperCase();
    }
}
