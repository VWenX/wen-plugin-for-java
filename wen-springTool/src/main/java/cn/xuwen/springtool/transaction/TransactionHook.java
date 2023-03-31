package cn.xuwen.springtool.transaction;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

/**
 * 事务钩子
 * 简化对TransactionSynchronizationManager.registerSynchronization的调用
 */
public class TransactionHook {

    public static void afterCommit(Runnable callback) {
        TransactionSynchronizationManager.registerSynchronization(
                new TxSyncAdapter().setAfterCommit(callback)
        );
    }

    public static void afterCompletion(Consumer<Integer> callback) {
        TransactionSynchronizationManager.registerSynchronization(
                new TxSyncAdapter().setAfterCompletion(callback)
        );
    }

    public static void registerSynchronization(TxSyncAdapter txSync){
        TransactionSynchronizationManager.registerSynchronization(txSync);
    }

}
