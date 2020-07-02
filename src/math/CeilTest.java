package math;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
//两个整数相除,如果有余数则取整数再加一
public class CeilTest {
    public static void main(String[] args) {
        int a= 1527;
        int b = 100;
        int c = a%b == 0 ? (a/b) : (a/b)+1;
        System.out.println(c );
    }
}
