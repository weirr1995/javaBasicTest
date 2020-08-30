package thread.threadPoolExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000));
        for (int i = 0; i < 20; i++) {
            Map params = new HashMap<String, String>();
            CheckDiffTask myTask = new CheckDiffTask(params);
            if (!executor.isShutdown()) {
                executor.execute(myTask);
            }
        }
        executor.shutdown();

        //等待返回结果
        while (true) {
            if (executor.isTerminated()) {
                break;
            }
            Thread.sleep(200);//这句必须要，不然可能造成内存持续占用
        }
    }
}
