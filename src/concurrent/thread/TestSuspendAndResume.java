package concurrent.thread;

/**
 * @author linjing
 * @date: Created in 2020/6/23
 */
public class TestSuspendAndResume {
    /**
     * 挂起和唤醒线程，suspend e()使线程进入阻塞状态，只有对应的resume e()被调用的时候，线程才会进入可执行状态。(弃用，容易发生死锁)
     * 下面就是发生死锁独占资源的例子
     */
    public static void main(String[] args) {
        try {
            final SynchronizedObject object = new SynchronizedObject();

            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    object.printString();
                }
            };

            thread1.setName("a");
            thread1.start();

            Thread.sleep(1000);

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    System.out.println("thread2 启动了，但进入不了 printString() 方法！只打印 1 个 begin");
                    System.out.println("因为 printString() 方法被 a 线程锁定并且永远 suspend 暂停了");
                    object.printString();
                }
            };
            thread2.start();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class SynchronizedObject {
    synchronized public void printString() {
        System.out.println("begin");
        if (Thread.currentThread().getName().equals("a")) {
            System.out.println("a 线程永远 suspend 了");
            Thread.currentThread().suspend();
        }
        System.out.println("end");
    }
}
