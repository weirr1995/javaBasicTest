package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/7/14
 */
@Slf4j
public class TimedWaiting_WaitLong {
    @Test
    public void test() throws InterruptedException {
        synchronized (this){
            log.debug("开始运行");
            wait(1000);
            log.debug("结束运行");
        }

    }
}
