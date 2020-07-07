package concurrent.thread;

import java.io.IOException;

/**
 * @version V1.0
 * @author: zouwh
 * @description: java层面 6种线程状态  新建  运行（运行 限时等待 不限时等待 阻塞） 终止态
 * @date: 2020/7/5 2:36 PM
 */
public class JavaThreadSixStatus {
    public static void main(String[] args) {
        //新建
        Thread thread1 = new Thread();
        //运行
        Thread thread2 = new Thread(()->{
            while (true){
            }
        });
        thread2.start();
        //限时等待:不释放锁
        Thread thread3 = new Thread(()->{
            synchronized (JavaThreadSixStatus.class) {
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread3.start();
        //不限时等待：不释放锁
        Thread thread4 = new Thread(()->{
            try {
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread4.start();
        //阻塞：释放锁，让出CPU
        Thread thread5 = new Thread(()->{
            synchronized (JavaThreadSixStatus.class) {
                try {
                    int read = System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread5.start();

        //终止态
     Thread thread6 = new Thread(()->{
            int i=0;
        });
        thread6.start();

        System.out.println(thread1.getState());
        System.out.println(thread2.getState());
        System.out.println(thread3.getState());
        System.out.println(thread4.getState());
        System.out.println(thread5.getState());
        System.out.println(thread6.getState());

    }
}
