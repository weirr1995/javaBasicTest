package concurrent.threadPoolExecutor;

import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
public class CheckDiffTask implements Runnable {
    private Map initMap;
    private static final String schema = "gjdfppos";

    public CheckDiffTask(Map params) {
        this.initMap = initMap;
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName());

    }
}
