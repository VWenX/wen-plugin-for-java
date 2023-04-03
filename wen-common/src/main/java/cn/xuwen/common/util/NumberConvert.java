package cn.xuwen.common.util;

import java.math.BigDecimal;

/**
 * @author XuWen
 */
public class NumberConvert {


    public static Double toDouble(String val){
        if (val==null || val.isEmpty()) return null;
        return Double.valueOf(val);
    }

    public static double toDouble(String val, double def){
        if (val==null || val.isEmpty()) return def;
        return Double.parseDouble(val);
    }

    public static double toDouble(Number val, double def){
        if (val==null) return def;
        return val.doubleValue();
    }

    public static BigDecimal toBigDecimal(String val){
        if (val == null) return null;
        return new BigDecimal(val);
    }

    public static BigDecimal toBigDecimal(Double val){
        if (val == null) return null;
        return BigDecimal.valueOf(val);
    }

    public static Integer toInteger(Number val){
        if (val == null) return null;
        return val.intValue();
    }

    public static Integer toInteger(String val, Integer emptyDefault){
        if (val == null || val.isEmpty()) return emptyDefault;
        return Integer.valueOf(val);
    }

    public static Integer toInteger(String val){
        return toInteger(val, null);
    }

    public static Long toLong(Number val) {
        if (val == null) return null;
        return val.longValue();
    }
}
