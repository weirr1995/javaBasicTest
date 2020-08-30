package thread;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 锁静态方法等于锁类，锁同一个类相当于互斥锁
 * 结果：2，1
 */
public class Synchronized03_Test8Locks_07 {

    public static void main(String[] args) {
        Number07 number1 = new Number07();
        Number07 number2 = new Number07();
        Thread thread1 = new Thread(()->number1.a());
        Thread thread2 = new Thread(()->number2.b());
        thread1.start();
        thread2.start();
    }
}


class Number07{
    public static synchronized void a(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1....");
    }

    public  synchronized void b(){

        System.out.println("2....");
    }

}
