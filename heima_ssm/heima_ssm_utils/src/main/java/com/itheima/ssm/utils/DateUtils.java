package com.itheima.ssm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换时间格式的工具类
 */
public class DateUtils {
    /**
     * 时间转换成字符串
     * @param date
     * @param patt
     * @return
     */
    public static String dateToString(Date date,String patt){
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        // 格式化
        String format = sdf.format(date);
        return format;
    }

    public static Date stringToDate(String string,String patt) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(patt);
        // 解析
        Date parse = sdf.parse(string);
        return parse;
    }
}
