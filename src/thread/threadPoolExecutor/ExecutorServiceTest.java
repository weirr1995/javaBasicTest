package thread.threadPoolExecutor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author linjing
 * @date: Created in 2020/7/2
 */

/**
 *All Superinterfaces:
 * Executor
 * All Known Subinterfaces:
 * ScheduledExecutorService
 * All Known Implementing Classes:
 * AbstractExecutorService, ForkJoinPool, ScheduledThreadPoolExecutor, ThreadPoolExecutor
 */

/**
 * 1、线程池： 提供一个线程队列，队列中保存着所有等待状态的线程。避免了创建与销毁的额外开销，提高了响应的速度。
 *
 * 2、线程池的体系结构：
 * java.util.concurrent.Executor 负责线程的使用和调度的根接口
 * 		|--ExecutorService 子接口： 线程池的主要接口
 * 				|--ThreadPoolExecutor 线程池的实现类
 * 				|--ScheduledExceutorService 子接口： 负责线程的调度
 * 					|--ScheduledThreadPoolExecutor : 继承ThreadPoolExecutor，实现了ScheduledExecutorService
 *
 *
 * 3、工具类 ： Executors
 * ExecutorService newFixedThreadPool() : 创建固定大小的线程池
 * ExecutorService newCachedThreadPool() : 缓存线程池，线程池的数量不固定，可以根据需求自动的更改数量。
 * ExecutorService newSingleThreadExecutor() : 创建单个线程池。 线程池中只有一个线程
 *
 * ScheduledExecutorService newScheduledThreadPool() : 创建固定大小的线程，可以延迟或定时的执行任务
 */
@RunWith(SpringJUnit4ClassRunner.class)//用来加载spring配置
@ContextConfiguration("classpath:applicationContext.xml")//classpath:表示从根路径查找xml文件
public class ExecutorServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void test() throws Exception{
        StringBuffer sql = new StringBuffer("select c_fundcode  from vtopenperiod_check_fundlist");
        List<String> fundList = jdbcTemplate.queryForList(sql.toString(), String.class, null);

        Map<Integer, List<String>> batchMap = new HashMap<Integer, List<String>>();
        int batchNo = 0;
        int batchSize = 100;
        for (int i = 0; ; ) {
            if (fundList.size() - i <= batchSize) {
                batchNo++;
                batchMap.put(batchNo, fundList.subList(i, fundList.size()));
                break;
            }
            batchNo++;
            batchMap.put(batchNo, fundList.subList(i, i + batchSize));
            i = i + batchSize;
        }
        int threadCount = batchMap.size();

        ExecutorService executors = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (final Map.Entry<Integer, List<String>> entry : batchMap.entrySet()) {
            executors.submit((new Runnable() {
                public void run() {
                    System.out.println("开始执行第" + entry.getKey() + "批次");
                    List<String> list = entry.getValue();
                    for (String fundCode : list) {
                        if (!StringUtils.isEmpty(fundCode)) {
                            doSomeThings(fundCode);
                        }
                    }
                    latch.countDown();
                    System.out.println("结束执行第" + entry.getKey() + "批次");
                }
            }));
        }
        latch.await();
    }
    public void doSomeThings(String fundCode){
        System.out.println(fundCode);
    }
}
