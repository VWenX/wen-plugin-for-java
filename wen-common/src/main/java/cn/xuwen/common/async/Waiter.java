package cn.xuwen.common.async;

/**
 * 等待者
 * @author XuWen
 * @param <R> 可承载一个泛型结果，由完成方设置，等待者获取
 */
public class Waiter<R> {

    /** 是否已完成 */
    private volatile boolean finished = false;
    /** 完成者设置的结果 */
    private R result = null;

    /**
     * 等待完成
     * @return 完成者设置的结果
     * @throws InterruptedException wait(前/中)发生中断,将抛出此异常
     */
    public R waitFinish() throws InterruptedException {
        return waitFinish(0);
    }

    /**
     * 等待完成
     * @param timeout wait超时时间
     * @return 完成者设置的结果
     * @throws InterruptedException wait(前/中)发生中断,将抛出此异常
     */
    public final R waitFinish(long timeout) throws InterruptedException {

        SyncControl.syncWait(this, ()->this.finished, timeout);

        return result;
    }

    /**
     * 调用即表示已完成
     */
    public final void onFinish() {
        onFinish(null);
    }

    /**
     * 调用即表示已完成
     * @param r 设置供等待者获取的结果
     */
    public final void onFinish(R r) {
        synchronized (this){
            if (finished){
                throw new RuntimeException("Waiter不可重用");
            }
            result = r;
            finished = true;
            this.notifyAll();
        }
    }

}
