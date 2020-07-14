package concurrent.thread;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 认识synchronized关键字
 * @date: 2020/7/5 3:21 PM
 */
/**
 * 临界区： 一个实例内，有读又有写
 * 竞态：   同一时刻，多线程运行临界区代码
 * 解决线程出现竞态条件的问题：
 * synchronized(对象){//线程1 线程2（Blocked）
 *}
 */
public class Block_Synchronized {

    private   int i=0;
    private  static  Object room = new Object();

    public static void main(String[] args) throws Exception {
        Block_Synchronized demo01 = new Block_Synchronized();
        Thread thread1 = new Thread(()->{
            for(int j=0;j<5000;j++){
                synchronized (room){
                    demo01.i++;
                }
            }
        });

        Thread thread2 = new Thread(()->{
            for(int j=0;j<5000;j++){
                synchronized (room){
                    demo01.i--;
                }
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println(demo01.i);
    }


}
