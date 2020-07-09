package concurrent.thread;

import java.util.ArrayList;

/**
 * @version V1.0
 * @author: zouwh
 * @description: 测试局部变量的线程安全问题，当局部变量为引用对象或者读写局部变量的方法为public时有安全问题
 * 子类有可能与父类共享资源 ==局部变量暴露引用==jvm逃逸分析
 * 私有修饰符能保护线程安全
 * @date: 2020/7/5 9:20 PM
 */
public class ThreadSafe_LocalVariable {
    private static int LOOP_NUMBER = 200;
    private static int THREAD_NUMBER = 2;

    public static void main(String[] args) {
        ListUnSafe test = new TestSubClass();
        for(int i=0;i<THREAD_NUMBER;i++){
            new Thread(()->{
                test.method1(LOOP_NUMBER);
            }, "threadtest" +i);
        }
    }
}

class ListSafe {
    public final void method1(int loopNumber) {
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<loopNumber;i++){
            method2(arrayList);
            method3(arrayList);
        }
    }

    private void method2(ArrayList<String> arrayList) {
        arrayList.add("1");
    }

    private void method3(ArrayList<String> arrayList) {
        arrayList.remove(0);
    }
}

class ListUnSafe{
    public void method1(int loopNumber) {
        ArrayList<String> arrayList = new ArrayList<>();
        for(int i=0;i<loopNumber;i++){
            method2(arrayList);
            method3(arrayList);
        }
    }

    public void method2(ArrayList<String> arrayList) {
        arrayList.add("1");

    }
    public void method3(ArrayList<String> arrayList) {
        arrayList.remove(0);
    }
}

class TestSubClass extends ListUnSafe{
    @Override
    public void method3(ArrayList<String> arrayList) {
        new Thread(()->{
            arrayList.remove(0);
        });
    }
}