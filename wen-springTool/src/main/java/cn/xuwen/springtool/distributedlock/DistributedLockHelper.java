package cn.xuwen.springtool.distributedlock;


import cn.xuwen.common.annotation.Note;
import cn.xuwen.common.util.ArrayTool;
import cn.xuwen.springtool.transaction.TransactionHook;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Arrays;

@Note("可使用@Import注入容器")
@RequiredArgsConstructor
public class DistributedLockHelper {

    private final RedissonClient redisson;


    /**
     * 锁在当前事务上，事务完成(提交/回滚)后自动解锁
     */
    public RLock lockOnCurrentTx(String lockKey){
        RLock lock = redisson.getLock(lockKey);
        lock.lock();
        try{
            TransactionHook.afterCompletion((status) -> {
                lock.unlock();
            });
        }catch (Throwable ex){
            // 中途异常，释放所有锁
            lock.unlock();
            throw ex;
        }
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
            for (RLock lock : locks) {
                if (lock.isLocked()) lock.unlock();
            }
            throw ex;
        }
        return locks;
    }


}
