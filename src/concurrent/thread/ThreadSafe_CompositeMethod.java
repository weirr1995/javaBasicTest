package concurrent.thread;

import java.util.Hashtable;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 线程的所有方法都是线程安全的，调用线程的组合方法就是线程安全的吗
 * @date: 2020/7/5 9:59 PM
 * 结果：put时，并非只有一个线程执行了
 */
public class ThreadSafe_CompositeMethod {

    public static void main(String[] args) {
        MyUnSafeTable test = new MyUnSafeTable();
        // MySafeTable test = new MySafeTable();
        for(int i=0;i<5;i++){
            new Thread(()->{
                test.put("name","linjing");
            }, "threadtest" +i).start();

        }
    }


}

class MyUnSafeTable {
    Hashtable<String,String> myTable = new Hashtable<>();
    public void put(String Key,String value){
        if (myTable.get(Key) == null) {
            System.out.println(Thread.currentThread().getName()+"进入了Put方法。。。");
            myTable.put(Key,value);
        }
    }
}

class MySafeTable{
    Hashtable<String,String> myTable = new Hashtable<>();
    public synchronized  void put(String Key,String value){
        if (myTable.get(Key) == null) {
            System.out.println(Thread.currentThread().getName()+"进入了Put方法。。。");
            myTable.put(Key,value);
        }
    }
}



