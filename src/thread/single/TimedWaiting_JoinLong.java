package thread.single;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/7/14
 */
@Slf4j
public class TimedWaiting_JoinLong {
    @Test
    public void test(){
        Thread t1 = new Thread(()->{
            log.debug("t1线程开始运行，t1睡眠3s");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t1线程运行结束");
        },"t1");
        t1.start();

        Thread t2 = new Thread(()->{
            try {
                log.debug("t1线程加入到t2线程。。。");
                t1.join();
                log.debug("t2线程运行结束。。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2");
        t2.start();

        try {
            log.debug("t2线程加入到main线程。。。");
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("main线程运行结束。。。");


    }
}
