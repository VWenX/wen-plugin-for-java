package cn.xuwen.common.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 处理器池
 *   针对可复用但非线程安全的处理器实例提供优化（如：DateTimeFormatter）
 *   创建时定义处理器的提供实现，当需要处理器时将自动调用提供实现获取。
 *     如：()->DateTimeFormatter.ofPattern("yyyy-MM-dd")
 *   定义的池大小是持久池的容量，并不代表同时存在处理器的上限。
 *     如实例数已达到池大小但无可用实例，仍会创建新实例。交还给池时超出池大小的实例将被丢弃。
 * @author XuWen
 */
public class ProcessorPool<T, R> {

    private final Supplier<T> processorSupplier;

    private final LinkedBlockingQueue<T> instanceQueue;


    /**
     * @param processorSupplier 处理器提供者
     * @param poolSize 持久池大小
     */
    public ProcessorPool(Supplier<T> processorSupplier, int poolSize) {
        this.processorSupplier = processorSupplier;
        this.instanceQueue = new LinkedBlockingQueue<>(poolSize);
    }

    /**
     * 执行处理
     * @param handler 处理实现(将传入一个处理器)
     */
    public R process(Function<T, R> handler){
        T poll = null;
        try{
            poll = getInstance();
            return handler.apply(poll);
        }finally {
            if (poll != null) {
                instanceQueue.offer(poll);
            }
        }
    }

    /**
     * 获取可用实例
     */
    protected T getInstance(){
        T poll = instanceQueue.poll();
        if (poll == null){
            poll = processorSupplier.get();
        }
        return poll;
    }

}
