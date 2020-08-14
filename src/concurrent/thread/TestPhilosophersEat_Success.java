package concurrent.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author linjing
 * @date: Created in 2020/7/16
 * 成功解决哲学家吃饭问题的方法：
 * 1、允许最多 4 个哲学家同时坐在桌子上。
 * 2、只有一个哲学家的两根筷子都可用时，他才能拿起它们（他必须在临界区内拿起两根 辕子)。
 * 3、使用非对称解决方案。即单号的哲学家先拿起左边的筷子，接着右边的筷子；而双 号的哲学家先拿起右边的筷子，接着左边的筷子。
 *
 * 这里我们用第2种解决方法实现
 */

public class TestPhilosophersEat_Success {
    @Test
    public void test() throws IOException {
        ChopstickLock chopstick1 = new ChopstickLock("1");
        ChopstickLock chopstick2 = new ChopstickLock("2");
        ChopstickLock chopstick3 = new ChopstickLock("3");
        ChopstickLock chopstick4 = new ChopstickLock("4");
        ChopstickLock chopstick5 = new ChopstickLock("5");


        //重点一：哲学家的筷子排列问题，代表获取锁的顺序,使用RenentrantLock与顺序无关了。
        SmartPhilosopher philosopher1 = new SmartPhilosopher("苏格拉底",chopstick1,chopstick2);
        SmartPhilosopher philosopher2 = new SmartPhilosopher("柏拉图",chopstick2,chopstick3);
        SmartPhilosopher philosopher3 = new SmartPhilosopher("亚里士多德",chopstick3,chopstick4);
        SmartPhilosopher philosopher4 = new SmartPhilosopher("孔子",chopstick4,chopstick5);
        SmartPhilosopher philosopher5 = new SmartPhilosopher("哥白尼",chopstick5,chopstick1);


        philosopher1.start();
        philosopher2.start();
        philosopher3.start();
        philosopher4.start();
        philosopher5.start();


        System.in.read();

    }
}

@Slf4j
class SmartPhilosopher extends Thread{
    private String name;
    private ChopstickLock left;
    private ChopstickLock right;

    public SmartPhilosopher(String name, ChopstickLock left, ChopstickLock right) {
        this.name = name;
        this.left = left;
        this.right = right;
    }

    public void thinking(){
        Random r=new Random();
        try {
            //重点二：这里要写成睡眠同样固定时间，增加竞争的几率
            Thread.sleep(1000);
            //Thread.sleep(RandomLongWithRange.nextLong(r,3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void eat(){
        Random r=new Random();
        //log.debug(this.name+"使用筷子("+left.getName()+","+right.getName()+")吃饭了。。。");
        log.debug(this.name+"吃饭了。。。");
        try {
            // Thread.sleep(RandomLongWithRange.nextLong(r,5000));
            //重点二：这里要写成睡眠同样固定时间，增加竞争的几率
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //重点三：可以不思考一致争抢，增加死锁几率
        while(true){
            thinking();
            if(left.tryLock()){
                try{
                    if(right.tryLock()){
                        try{
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }
                }finally {
                    left.unlock();
                }

            }
        }
    }
}

final class ChopstickLock extends ReentrantLock {
    private String name;
    public ChopstickLock(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

