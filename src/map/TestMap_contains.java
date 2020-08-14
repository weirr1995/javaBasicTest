package map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author linjing
 * @date: Created in 2020/7/30
 */

public class TestMap_contains {
    @Test
    public  void test(){
        Map map = new HashMap<>();
        map.put("AAA","111");
        System.out.println(map.containsKey("aaa"));
    }
}
