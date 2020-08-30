package thread;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * C方法不会互斥
 * 结果： 3 1s 12
 *       23 1s 1
 *       32 1s 1
 */
public class Synchronized03_Test8Locks_03 {

    public static void main(String[] args) {
        Number03 number = new Number03();
        Thread thread1 = new Thread(()->number.a());
        Thread thread2 = new Thread(()->number.b());
        Thread thread3 = new Thread(()->number.c());
        thread1.start();
        thread2.start();
        thread3.start();
    }
}


class Number03{
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
    public  void c(){
        System.out.println("3....");
    }

}
