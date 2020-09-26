package thread.single;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/6/23
 */
@Slf4j
public class Waiting_Join {
    /**
     * 也叫线程加入。是当前线程A调用另一个线程B的join()方法，当前线程转A入阻塞状态，直到线程B运行结束，线程A才由阻塞状态转为可执行状态。
     */
    @Test
    public void test() throws InterruptedException {
      Thread t1 =   new Thread(()->{
            log.debug("线程1开始执行。。。");
            try {
                Thread.sleep(2000);
                log.debug("线程1睡眠结束。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");
      Thread t2 =   new Thread(()->{
            log.debug("线程2开始执行。。。");
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("主线程结束。。。");
    }
}
