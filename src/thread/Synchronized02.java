package thread;

/**
 * @version V1.0
 * @author: zouwh
 * @description: synchronized 针对synchronized01类的方法的改进
 * @date: 2020/7/5 3:21 PM
 */
public class Synchronized02 {
    public static void main(String[] args) throws Exception {
        Room room = new Room();
        Thread thread1 = new Thread(()->{
            for(int j=0;j<5000;j++){
                room.increment();
            }
        });

        Thread thread2 = new Thread(()->{
            for(int j=0;j<5000;j++){
                room.decrement();
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println(room.getCounter());
    }


}

  class  Room{
    private  int counter=0;

    public void increment(){
        synchronized (this){
            this.counter++;
        }
    }

    public void decrement(){
        synchronized (this){
            this.counter--;
        }
    }

    public int getCounter(){
        synchronized (this){
            return this.counter;
        }
    }
}