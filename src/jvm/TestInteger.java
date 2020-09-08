package jvm;

import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/9/8
 * 栈中的数据可以共享 ，int 类型【-128至127】这个范围内 在常量池中，超出了这个范围就放到了 堆中
 */
public class TestInteger {
    @Test
    public void test(){
        Integer a = 10;
        Integer b = 10;
        System.out.println("a == b  " + (a == b));
    }

    @Test
    public void test2(){
        Integer a = 190;
        Integer b = 190;
        System.out.println("a == b  " + (a == b));
    }
}
