package thread.single;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 线程阻塞不会释放锁,sleep方法不会释放锁
 * 结果： 1s后1,2 或 2 1s后打印1
 */
public class Synchronized03_Test8Locks_02 {

    public static void main(String[] args) {
        Number02 number = new Number02();
        Thread thread1 = new Thread(()->number.a());
        Thread thread2 = new Thread(()->number.b());
        thread1.start();
        thread2.start();
    }
}


class Number02{
    public synchronized void a(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1....");
    }

    public synchronized void b(){
        System.out.println("2....");
    }

}
