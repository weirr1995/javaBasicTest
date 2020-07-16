package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import math.RandomLongWithRange;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

/**
 * @author linjing
 * @date: Created in 2020/7/16
 * 演示哲学家吃饭的死锁问题
 */
@Slf4j
public class TestPhilosophersEat_DeadLock {

    @Test
    public void test() throws IOException {
        Chopstick chopstick1 = new Chopstick("1");
        Chopstick chopstick2 = new Chopstick("2");
        Chopstick chopstick3 = new Chopstick("3");
        Chopstick chopstick4 = new Chopstick("4");
        Chopstick chopstick5 = new Chopstick("5");

        Philosopher philosopher1 = new Philosopher("苏格拉底",chopstick5,chopstick1);
        Philosopher philosopher2 = new Philosopher("柏拉图",chopstick1,chopstick2);
        Philosopher philosopher3 = new Philosopher("亚里士多德",chopstick3,chopstick4);
        Philosopher philosopher4 = new Philosopher("孔子",chopstick3,chopstick4);
        Philosopher philosopher5 = new Philosopher("哥白尼",chopstick4,chopstick5);


        philosopher1.start();
        philosopher2.start();
        philosopher3.start();
        philosopher4.start();
        philosopher5.start();


        System.in.read();

    }
}
@Slf4j
class Philosopher extends Thread{
    private String name;
    private Chopstick left;
    private Chopstick right;

    public Philosopher(String name, Chopstick left, Chopstick right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public void thinking(){
        Random r=new Random();
        log.debug(this.name+"思考中。。。。");
        try {
            Thread.sleep(RandomLongWithRange.nextLong(r,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug(this.name+"饿了，准备拿起筷子吃饭。。。。");

    }

    public void eat(){
        Random r=new Random();
        log.debug(this.name+"开始吃饭了。。。");
        try {
            Thread.sleep(RandomLongWithRange.nextLong(r,5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug(this.name+"吃完饭了。。。");

    }

    @Override
    public void run() {
       while (true){
           thinking();
           synchronized (left){
               synchronized (right){
                   eat();
               }
           }
           log.debug(this.name+"放下筷子。。。");
       }
    }
}

class Chopstick{
    private String name;
    public Chopstick(String name) {
        this.name = name;
    }
}