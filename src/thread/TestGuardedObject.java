package thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

/**
 * @author linjing
 * @date: Created in 2020/7/8
 */
@Slf4j
public class TestGuardedObject {

    @Test
    public void test() throws IOException {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            log.debug("t1开始启动。。");
            String result =(String) guardedObject.get();
            log.debug("t1拿到结果值为{}",result);
        },"t1").start();

        new Thread(()->{
            log.debug("t2开始启动。。");
            guardedObject.compelete();
            try {
                Thread.sleep(2000);
                log.debug("t2执行另一个任务。。");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("t2任务执行结束。。");
        },"t2").start();

        System.in.read();
    }

}


@Slf4j
class GuardedObject{
    private String result;

    public String get(){
        synchronized (this){
            while (true){
                if(result == null){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return this.result;
            }
        }

    }

    public void compelete(){
        synchronized (this){
            try {
                Thread.sleep(1000);
                this.result = "执行成功！" ;
                this.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.result = "异常打断！";
            }
        }
    }
}