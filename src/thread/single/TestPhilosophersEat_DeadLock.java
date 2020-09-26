package thread.single;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

/**
 * @author linjing
 * @date: Created in 2020/7/16
 * 演示哲学家吃饭的死锁问题
 * //死锁重点一：哲学家的筷子排列问题，代表获取锁的顺序，一定要都是先获取左边，再获取右边
 * //重点二：吃饭时要写成睡眠同样固定时间，增加竞争的几率
 * //重点三：可以不思考一致争抢，增加死锁几率
 */
@Slf4j
public class TestPhilosophersEat_DeadLock {

    @Test
    public void deadLock() throws IOException {
        Chopstick chopstick1 = new Chopstick("1");
        Chopstick chopstick2 = new Chopstick("2");
        Chopstick chopstick3 = new Chopstick("3");
        Chopstick chopstick4 = new Chopstick("4");
        Chopstick chopstick5 = new Chopstick("5");


        //死锁重点一：哲学家的筷子排列问题，代表获取锁的顺序
        Philosopher philosopher1 = new Philosopher("苏格拉底",chopstick1,chopstick2);
        Philosopher philosopher2 = new Philosopher("柏拉图",chopstick2,chopstick3);
        Philosopher philosopher3 = new Philosopher("亚里士多德",chopstick3,chopstick4);
        Philosopher philosopher4 = new Philosopher("孔子",chopstick4,chopstick5);
        //死锁写法
        Philosopher philosopher5 = new Philosopher("哥白尼",chopstick5,chopstick1);
        //饥饿写法如下
        //Philosopher philosopher5 = new Philosopher("哥白尼",chopstick1,chopstick5);



        philosopher1.start();
        philosopher2.start();
        philosopher3.start();
        philosopher4.start();
        philosopher5.start();


        System.in.read();

    }

    @Test
    public void hungry() throws IOException {
        Chopstick chopstick1 = new Chopstick("1");
        Chopstick chopstick2 = new Chopstick("2");
        Chopstick chopstick3 = new Chopstick("3");
        Chopstick chopstick4 = new Chopstick("4");
        Chopstick chopstick5 = new Chopstick("5");


        //死锁重点一：哲学家的筷子排列问题，代表获取锁的顺序
        Philosopher philosopher1 = new Philosopher("苏格拉底",chopstick1,chopstick2);
        Philosopher philosopher2 = new Philosopher("柏拉图",chopstick2,chopstick3);
        Philosopher philosopher3 = new Philosopher("亚里士多德",chopstick3,chopstick4);
        Philosopher philosopher4 = new Philosopher("孔子",chopstick4,chopstick5);
        //饥饿写法如下
        Philosopher philosopher5 = new Philosopher("哥白尼",chopstick1,chopstick5);



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
       while (true){
           //重点三：可以不思考一致争抢，增加死锁几率
          // thinking();
           synchronized (left){
               synchronized (right){
                   eat();
               }
           }
       }
    }
}

class Chopstick{
    private String name;
    public Chopstick(String name) {
        this.name = name;
    }
}