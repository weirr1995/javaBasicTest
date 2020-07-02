package concurrent.threadPoolExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
public class Terminated {
    public static void main(String args[]) throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1000));
        for (int i = 1; i <= 10; i++) {
            Map params = new HashMap<String,String>();
            CheckDiffTask myTask = new CheckDiffTask(params);
            executor.execute(myTask);
        }
        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(200);
        }
    }
}
