package cn.xuwen.common.util;

import java.util.function.Function;

public class ArrayTool {

    public static <T, R> R[] mapCollect(T[] sourceArr, Function<T, R> mapping){
        R[] rs = (R[]) new Object[sourceArr.length];
        for (int i = 0; i < sourceArr.length; i++) {
            rs[i] = mapping.apply(sourceArr[i]);
        }
        return rs;
    }

}
