package cn.xuwen.springtool.transaction;

import org.springframework.transaction.support.TransactionSynchronization;

import java.util.function.Consumer;

/**
 * 可自由定制的事务同步回调适配器
 * 避免多处代码中繁琐地编写匿名内部类
 */
public class TxSyncAdapter implements TransactionSynchronization {

    private int order = TransactionSynchronization.super.getOrder();
    private Runnable afterCommit = null;
    private Consumer<Integer> afterCompletion = null;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void afterCommit() {
        if (afterCommit != null) afterCommit.run();
    }

    @Override
    public void afterCompletion(int status) {
        if (afterCompletion != null) afterCompletion.accept(status);
    }


    public TxSyncAdapter setOrder(int order) {
        this.order = order;
        return this;
    }

    public TxSyncAdapter setAfterCommit(Runnable afterCommit) {
        this.afterCommit = afterCommit;
        return this;
    }

    public TxSyncAdapter setAfterCompletion(Consumer<Integer> afterCompletion) {
        this.afterCompletion = afterCompletion;
        return this;
    }
}