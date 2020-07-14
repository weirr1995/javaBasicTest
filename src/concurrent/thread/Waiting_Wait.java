package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/6/23
 */
@Slf4j
public class Waiting_Wait {
    /**
     * 两个方法搭配使用，wait()使线程进入阻塞状态，调用notify()时，线程进入可执行状态。wait()内可加或不加参数，加参数时是以毫秒为单位，当到了指定时间或调用notify()方法时，进入可执行状态。(属于Object类，而不属于Thread类，wait()会先释放锁住的对象，然后再执行等待的动作。由于wait()所等待的对象必须先锁住，因此，它只能用在同步化程序段或者同步化方法内，否则，会抛出异常IllegalMonitorStateException.)
     */
    @Test
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(()->{
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread.sleep(1000);
        Thread t2 = new Thread(()->{
            t1.notify();
        });
        t2.start();
    }

    @Test
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(()->{
            synchronized (Waiting_Wait.class){
                try {
                    log.debug("t1线程1s后进入阻塞状态。。。");
                    Thread.sleep(2000);
                    this.wait();
                    log.debug("t1线程被唤醒继续执行。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        t1.start();

        log.debug("主线程进入睡眠状态。。。");
        Thread.sleep(1000);

        Thread t2 = new Thread(()->{
            synchronized (Waiting_Wait.class) {
                log.debug("t2线程获得锁，唤醒t1。。。");
                t1.notify();
            }
        });
        t2.start();

        t1.join();
        t2.join();
    }
}
