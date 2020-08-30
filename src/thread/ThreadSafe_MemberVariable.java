package thread;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 线程安全问题示例
 * @date: 2020/7/5 3:16 PM
 * 一个线程对i变量加5000此，另一个线程对i变量减5000次，但结果不是0
 */
public class ThreadSafe_MemberVariable {

    public static void main(String[] args) throws Exception {
        CounterUnSafe counter = new CounterUnSafe();
      //  CounterSafe counter = new CounterSafe();
        Thread thread1 = new Thread(()->{
            for(int j=0;j<5000;j++){
                counter.increment();
            }
        });

        Thread thread2 = new Thread(()->{
            for(int j=0;j<5000;j++){
                counter.decrement();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println(counter.getCount());
    }

}


class CounterUnSafe {
    private   int count=0;
    public void increment(){
        count++;
    }
    public void decrement(){
        count--;
    }
    public int getCount(){
        return count;
    }

}

/**
 * @author: zouwh
 * @description:
 * @date:   2020/7/5
 * @version V1.0
 * 线程安全： 即多个线程调用同一个实例的某个方法时，是线程安全的
 * 误区：每个方法是线程安全的，所有方法组合在一起并不一定是线程安全的
 */
class CounterSafe {
    private  int count=0;

    public void increment(){
        synchronized (this){
            this.count++;
        }
    }

    public void decrement(){
        synchronized (this){
            this.count--;
        }
    }

    public int getCount(){
        synchronized (this){
            return this.count;
        }
    }

}



