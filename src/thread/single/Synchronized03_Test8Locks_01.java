package thread.single;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 锁方法等于锁对象,锁住同一个对象就是互斥效果，同理：锁静态方法等于锁类
 * 结果： 1，2 或  2，1
 */
public class Synchronized03_Test8Locks_01 {

    public static void main(String[] args) {
        Number01 number = new Number01();
        Thread thread1 = new Thread(()->number.a());
        Thread thread2 = new Thread(()->number.b());
        thread1.start();
        thread2.start();
    }
}


class Number01{
    public synchronized void a(){
        System.out.println("1....");
    }

    public synchronized void b(){
        System.out.println("2....");
    }

}
