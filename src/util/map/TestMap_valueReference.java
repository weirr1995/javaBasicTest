package util.map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/9/8
 */
public class TestMap_valueReference {
    @Test
    public void test(){
        Map map = new HashMap();
        test2(map);
        System.out.println(map.size());
        test3(map);
        System.out.println(map.size());
    }

    public void test2(Map map){
        Map map1 = new HashMap();
        map1.put("a","aa");
        map = map1;
    }

    public void test3(Map map){
        map.put("a","aa");
    }
}
