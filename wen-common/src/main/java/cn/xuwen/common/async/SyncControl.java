package cn.xuwen.common.async;

import java.util.function.BooleanSupplier;

/**
 * @author XuWen
 */
class SyncControl {

    /**
     * 同步等待
     * @param lockTarget 锁目标对象
     * @param isFinished  获取是否完成等待条件的实现
     * @param timeout wait超时
     * @throws InterruptedException wait(前/中)发生中断,将抛出此异常
     */
    public static void syncWait(Object lockTarget, BooleanSupplier isFinished, long timeout) throws InterruptedException {
        if (isFinished.getAsBoolean()){
            return;
        }

        // 超时时间线，用于确定唤醒原因是超时非虚假唤醒
        long timeLine = (timeout == 0) ? 0
                : System.currentTimeMillis() + timeout;

        synchronized (lockTarget){
            // while防止虚假唤醒
            while (!isFinished.getAsBoolean()){

                lockTarget.wait(timeout);

                // 未完成 && 设置了超时
                if (!isFinished.getAsBoolean() && timeLine != 0){
                    long timeDiff = System.currentTimeMillis() - timeLine;
                    // 已超时 跳出
                    if (timeDiff > -1){
                        break;
                    }else {
                        // 未超时，即为虚假唤醒 补偿超时时间
                        timeout = timeLine + timeDiff;
                    }
                }
            }
        }

        return;
    }


}
