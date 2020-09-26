package thread.single;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 * 锁静态方法等于锁类，锁同一个类相当于互斥锁
 * 结果：1 1s 1 ,2 1s 1
 */
public class Synchronized03_Test8Locks_08 {

    public static void main(String[] args) {
        Number08 number1 = new Number08();
        Number08 number2 = new Number08();
        Thread thread1 = new Thread(()->number1.a());
        Thread thread2 = new Thread(()->number2.b());
        thread1.start();
        thread2.start();
    }
}


class Number08{
    public static synchronized void a(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("1....");
    }

    public static  synchronized void b(){

        System.out.println("2....");
    }

}
