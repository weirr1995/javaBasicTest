package thread.single;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 锁方法等于锁对象,锁住同一个对象就是互斥效果，同理：锁静态方法等于锁类
 * 结果： 2,1
 */
public class Synchronized03_Test8Locks_04 {

    public static void main(String[] args) {
        Number04 number1 = new Number04();
        Number04 number2 = new Number04();
        Thread thread1 = new Thread(()->number1.a());
        Thread thread2 = new Thread(()->number2.b());
        thread1.start();
        thread2.start();
    }
}


class Number04{
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
