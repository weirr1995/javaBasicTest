package threadtest;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 锁静态方法等于锁类
 * 结果： 2,1
 */
public class Synchronized03_Test8Locks_05 {

    public static void main(String[] args) {
        Number05 number1 = new Number05();
        Thread thread1 = new Thread(()->number1.a());
        Thread thread2 = new Thread(()->number1.b());
        thread1.start();
        thread2.start();
    }
}


class Number05{
    public static synchronized void a(){
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
