package math;

import org.junit.Test;

/**
 * @author linjing
 * @date: Created in 2020/6/30
 */
//两个整数相除,如果有余数则取整数再加一
public class CeilTest {
    @Test
    public  void a1() {
        int a= 1527;
        int b = 100;
        int c = a%b == 0 ? (a/b) : (a/b)+1;
        System.out.println(c );
    }

    @Test
    public void test(){
        int nodeWidth =100,nodeHeight=30,arrowLen=15;
        int firstStartX = 20;
        int firstStartY = 20;
        int startX = firstStartX;
        int startY = firstStartY;
        int lineSpace = 20;

        for(int i=0;i<21;i++){
            int j = i%8+1;
            if(j==8){
                System.out.println("j:"+j+"---"+(Math.ceil((i+1)/8)-1)+"---"+(firstStartY+ (nodeHeight+lineSpace)*(Math.ceil((i+1)/8))));
            }else{
                System.out.println("j:"+j+"---");
            }
        }
    }


}
