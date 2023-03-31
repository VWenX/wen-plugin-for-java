package cn.xuwen.springtool.distributedlock;


import cn.xuwen.common.util.ArrayTool;
import cn.xuwen.springtool.transaction.TransactionHook;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class DistributedLockHelper {

    @Autowired
    RedissonClient redisson;


    /**
     * 锁在当前事务上，事务完成(提交/回滚)后自动解锁
     */
    public RLock lockOnCurrentTx(String lockKey){
        RLock lock = redisson.getLock(lockKey);
        lock.lock();
        TransactionHook.afterCompletion((status) -> {
            lock.unlock();
        });
        return lock;
    }

    /**
     * 锁在当前事务上，事务完成(提交/回滚)后自动解锁
     */
    public RLock[] lockMultiOnCurrentTx(String... lockKeys){
        // 排序，避免死锁 eg:1(a-d-c) 2(c-a)
        Arrays.sort(lockKeys);
        RLock[] locks = ArrayTool.mapCollect(lockKeys, redisson::getLock);

        try{
            for (RLock lock : locks) {
                lock.lock();
            }

            TransactionHook.afterCompletion((status) -> {
                for (RLock lock : locks) lock.unlock();
            });
        }catch (Throwable ex){
            // 中途异常，释放所有锁
            throw ex;
        }
        return locks;
    }


}
