package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * @author linjing
 * @date: Created in 2020/7/14
 */
@Slf4j
public class TimedWaiting_ParkNanos {
    @Test
    public  void test() throws InterruptedException {
        Thread t1 = new Thread(()->{
            log.debug("线程准备parkNanos,此时状态是{}",Thread.currentThread().getState());
            LockSupport.parkNanos(100000000);
        },"t1");
        t1.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("线程parkNanos,此时状态是{}",t1.getState());

        LockSupport.unpark(t1);
        log.debug("线程unpark,此时状态是{}???",t1.getState());
        t1.join();
    }
}
