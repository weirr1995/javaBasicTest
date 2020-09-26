package thread.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author linjing
 * @date: Created in 2020/7/7
 */
/**
 * 真正的并发 https://blog.csdn.net/xiamiflying/article/details/89915760
 */
@Slf4j
public class TestConcurrent02 {
    // 注意，此处是非线程安全的，留坑
    private int iCounter;
    private static final int THREAD_NUMBER = 5;
    final CountDownLatch startGate = new CountDownLatch(1);
    final CountDownLatch endGate = new CountDownLatch(THREAD_NUMBER);

    @Test
    public  void testConcurrent() throws InterruptedException {
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                try {
                    // 使线程在此等待，当开始门打开时，一起涌入门中
                    startGate.await();
                    try {
                        try {
                            //此处是让指令错配，更容易出现并发问题
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        iCounter++;
                        log.debug(" iCounter ={} " , iCounter);
                    } finally {
                        // 将结束门减1，减到0时，就可以开启结束门了
                        endGate.countDown();
                    }
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }).start();
        }

        //防止线程还才刚New出来，开始门就已经释放了。造成有部分线程await无法唤醒
        Thread.sleep(2000);

        long startTime = System.nanoTime();
        log.debug(" All thread is ready, concurrent going...");
        // 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        log.debug(" All thread is completed.");
        log.debug("并发耗时{}纳秒",endTime - startTime);
        log.debug("最终结果icounter={}",iCounter);
    }
}
