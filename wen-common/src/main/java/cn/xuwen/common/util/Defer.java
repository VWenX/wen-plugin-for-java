package cn.xuwen.common.util;


import cn.xuwen.common.annotation.Note;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *  defer系列： 使finally代码书写更靠前，避免相关性代码被逻辑代码穿插影响可读性。
 *  想法源于Golang的defer
 */
@Note({
        "执行顺序： { [init] -> exec } -> finallyHandler",
        "参数：",
        "init: 初始操作，比如：加锁",
        "finallyHandler: 最终操作，比如：解锁",
        "exec: 中间执行，比如：需要在锁内执行的逻辑代码",
})
public class Defer {

    public static void defer(Runnable finallyHandler, Runnable exec){
        try {
            exec.run();
        }finally {
            finallyHandler.run();
        }
    }

    public static <T> T defer(Runnable finallyHandler, Supplier<T> exec){
        try {
            return exec.get();
        }finally {
            finallyHandler.run();
        }
    }

    public static void defer(Runnable init, Runnable finallyHandler, Runnable exec){
        try {
            init.run();
            exec.run();
        }finally {
            finallyHandler.run();
        }
    }

    /**
     * 示例：
     *   lockObj = getLock();
     *   Defer.defer(
     *     () -> { lockObj.lock(); },
     *     () -> { lockObj.unLock(); },
     *     () -> { return res; }
     *   );
     */
    public static <T> T defer(Runnable init, Runnable finallyHandler, Supplier<T> exec){
        try {
            init.run();
            return exec.get();
        }finally {
            finallyHandler.run();
        }
    }

    /**
     * 示例：
     *   Defer.defer(
     *     () -> { return genLockedObj(); },
     *     lockObj -> { lockObj.unLock(); },
     *     () -> { return res; }
     *   );
     */
    public static <T, L> T defer(Supplier<L>  init, Consumer<L> finallyHandler, Supplier<T> exec){
        L l = null;
        try {
            l = init.get();
            return exec.get();
        }finally {
            finallyHandler.accept(l);
        }
    }

}