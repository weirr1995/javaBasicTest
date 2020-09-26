package thread.single;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @author  linjing
 * @date : Created in 2020/6/23
 *
 */

/*
sleep(毫秒)，指定以毫秒为单位的时间，使线程在该时间内进入线程阻塞状态，期间得不到cpu的时间片，等到时间过去了，线程重新进入可执行状态。(暂停线程，不会释放锁)
 */
@Slf4j
public class TimedWaiting_Sleep {
    @Test
    public void test1() throws InterruptedException {
        Thread t1 = new Thread(()->{
            log.debug("t1启动。。。");
            try {
                log.debug("t1开始睡眠。。。");
                Thread.sleep(2000);
                log.debug("t1结束睡眠。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t1.join();
    }

    @Test
    public void test2() throws InterruptedException, IOException {
        Thread t1 = new Thread(()->{
            log.debug("t1启动。。。");
            try {
                log.debug("t1开始睡眠。。。");
                Thread.sleep(2000);
                log.debug("t1结束睡眠。。。");
            } catch (InterruptedException e) {
                log.debug("重点： 。。。t1被打断后,打断标记为：{}",Thread.currentThread().isInterrupted());
                log.debug("t1被打断后的状态：{}",Thread.currentThread().getState());
                e.printStackTrace();
            }
        });
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();
        System.in.read();
    }

    @Test
    public void test3() throws InterruptedException, IOException {
        Thread t1 = new Thread(()->{
            log.debug("t1启动。。。");
            try {
                log.debug("t1开始睡眠。。。");
                Thread.sleep(2000);
                log.debug("t1结束睡眠。。。");
            } catch (InterruptedException e) {
                log.debug("重点： 。。。t1被异常打断后,打断标记为：{}",Thread.currentThread().isInterrupted());
                Thread.currentThread().interrupt();
                log.debug("重点： 。。。t1被正常打断后,打断标记为：{}",Thread.currentThread().isInterrupted());
                e.printStackTrace();
            }
        });
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();
        System.in.read();
    }

    @Test
    public void test4() throws InterruptedException, IOException {
        Thread t1 = new Thread(()->{
            log.debug("t1启动。。。");
            while (true){
                if(Thread.currentThread().isInterrupted()){
                    log.debug("重点： 。。。t1被打断,结束线程。。");
                    break;
                }
                try {
                    log.debug("t1开始睡眠。。。");
                    Thread.sleep(2000);
                    log.debug("t1结束睡眠。。。");
                } catch (InterruptedException e) {
                    log.debug("重点： 。。。t1被异常打断后,打断标记为：{}",Thread.currentThread().isInterrupted());
                    Thread.currentThread().interrupt();
                    log.debug("重点： 。。。t1被正常打断后,打断标记为：{}",Thread.currentThread().isInterrupted());
                    e.printStackTrace();
                }
            }

        });
        t1.start();

        Thread.sleep(1000);
        t1.interrupt();
        System.in.read();
    }
}
