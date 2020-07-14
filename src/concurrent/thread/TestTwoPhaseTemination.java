package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/7/3
 * 线程两阶段终止模式（优化的停止某个线程,stop()不优雅）
 * 被打断线程有料理后事的机会
 */
@Slf4j
public class TestTwoPhaseTemination {
    private Thread mointorThread;

    //启动监控线程
    public  void start(){
        mointorThread = new Thread(() ->{
            while(true){
                Thread current = Thread.currentThread();
                if(current.isInterrupted()){
                    log.info("料理后事。。。");
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.info("被打断了。。。");
                    //这句很重要，因为sleep被打断了，只会抛异常，不会改变打断标记，current.isInterrupted()还false，
                    // 需自己再手工打个打断标记才会退出循环
                    current.interrupt();
                    e.printStackTrace();
                }
            }
        });

        mointorThread.start();

    }

    //停止监控线程
    public void stop(){
        log.info("即将打断mointorThread线程。。。");
        mointorThread.interrupt();
    }

    @Test
    public  void test() {
        TestTwoPhaseTemination testTwoPhaseTemination = new TestTwoPhaseTemination();
        testTwoPhaseTemination.start();
        testTwoPhaseTemination.stop();
    }
}
