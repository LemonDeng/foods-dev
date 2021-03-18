package com.dzl.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileEmailUtils {

    /**
     * 检查手机的格式是否正确
     * @param mobile
     * @return
     */
    public static boolean checkMobileIsOk(String mobile) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        return isMatch;
    }

    /**
     * 检查邮箱的格式是否正确
     * @param email
     * @return
     */
    public static boolean checkEmailIsOk(String email) {
        boolean isMatch = true;
        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            isMatch = false;
        }
        return isMatch;
    }
}

