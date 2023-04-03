package cn.xuwen.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XuWen
 */
public class StringTool {

    /**
     * 驼峰转下划线 剔除开始的下划线
     * @param str 驼峰字符串
     * @return 下划线字符串
     */
    public static String humpTo_Ltrim(String str) {
        if (str == null || str.isEmpty()){
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            // A-Z 65-90
            if (c < 'A' || c > 'Z'){
                sb.append(c);
            }else {
                sb.append('_')
                        .append(Character.toLowerCase(c));
            }
        }
        if (sb.charAt(0) == '_'){
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }


    /**
     * 下划线转驼峰
     * @param str
     * @return
     */
    public static String _ToHump(String str){
        if (str == null || str.isEmpty()){
            return "";
        }

        boolean needHandle = false;
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c == '_'){
                needHandle = true;
                continue;
            }

            if ( !needHandle || c < 'a' || c > 'z'){
                sb.append(c);
            }else {
                needHandle = false;
                sb.append(Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }

    public static Map<String, String> parseQuery(String queryString){
        HashMap<String, String> queryMap = new HashMap<>();
        if (queryString == null || queryString.length() < 2){
            return queryMap;
        }

        if (queryString.startsWith("?")){
            queryString = queryString.substring(1);
        }

        for (String item : queryString.split("&")) {
            String[] split = item.split("=");
            if (split.length != 2){
                continue;
            }
            try {
                queryMap.put(split[0], URLDecoder.decode(split[1], "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return queryMap;
    }

    public static String getFirstNotEmpty(String... strings){
        for (String string : strings) {
            if (string != null && !string.isEmpty()){
                return string;
            }
        }
        return null;
    }

    public static String trimL(String text, String trimStr){
        if (trimStr != null && text.startsWith(trimStr)){
            return text.substring(trimStr.length());
        }
        return text;
    }

    /** 拼接字符串忽略连续的重复值 */
    public static String joinSkipRepeat(CharSequence delimiter, String... strings){
        StringBuilder sb = new StringBuilder();
        String last = null;
        for (String str : strings) {
            if (str == null || str.equals(last)){
                continue;
            }
            sb.append(delimiter).append(str);
            last = str;
        }

        if (sb.length() == 0){
            return "";
        }
        return sb.substring(delimiter.length());
    }

    /** 拼接跳过空 */
    public static String joinSkipEmpty(CharSequence delimiter, String... strings){
        StringBuilder sb = new StringBuilder();
        for (String str : strings) {
            if (str == null || str.isEmpty()){
                continue;
            }
            sb.append(delimiter).append(str);
        }

        if (sb.length() == 0){
            return "";
        }
        return sb.substring(delimiter.length());
    }

}
