package concurrent;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 华罗庚 统筹方法 烧水泡茶
 * @date: 2020/7/5 2:21 PM
 */
public class BoilingWaterAndMakeTea {
    /*
    * 洗茶壶(1min） 烧水（15min)  洗茶杯(1min） 拿茶叶(1min） 泡茶(1min）
    * 采用2个线程干活最优 ：
    *   线程一：洗茶壶(1min） 烧水（15min)
    *   线程二：洗茶杯(1min） 拿茶叶(1min） 泡茶(1min）
     */

    public static void main(String[] args) {
        Thread thread1 = new Thread(()->{
            System.out.println("洗茶壶");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("烧水");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"老王");

        Thread thread2 = new Thread(()->{
            System.out.println("洗茶杯");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("拿茶叶");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("泡茶");

        },"小王");

        thread1.start();
        thread2.start();
    }
}


