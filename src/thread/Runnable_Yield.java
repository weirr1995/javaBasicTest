package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/6/23
 */
@Slf4j
public class Runnable_Yield {
    /**
     * 会使的线程放弃当前分得的cpu时间片，但此时线程任然处于可执行状态，随时可以再次分得cpu时间片。yield()方法只能使同优先级的线程有执行的机会。调用 yield()的效果等价于调度程序认为该线程已执行了足够的时间从而转到另一个线程。(暂停当前正在执行的线程，并执行其他线程，且让出的时间不可知)
     */
    @Test
    public void test() throws InterruptedException {
        new Thread(()->{
            log.debug("t1线程开始运行");
            Thread.yield();
            log.debug("t1线程运行结束");
        },"t1").start();

        new Thread(()->{
            log.debug("t2线程开始运行");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2线程运行结束");
        },"t2").start();


        Thread.sleep(3000);
    }
}
