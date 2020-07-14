package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * @author linjing
 * @date: Created in 2020/7/14
 */
@Slf4j
public class Waiting_Park {

    @Test
    public  void test(){
       Thread t1 = new Thread(()->{
           log.debug("线程准备park,此时状态是{}",Thread.currentThread().getState());
            LockSupport.park();
        },"t1");
       t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("线程park,此时状态是{}",t1.getState());

        LockSupport.unpark(t1);
        log.debug("线程unpark,此时状态是{}",t1.getState());

    }
}
