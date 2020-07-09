package map;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author linjing
 * @date: Created in 2020/7/9
 */
@Slf4j
public class TestMap_Remove {

    @Test
    public void test(){
        log.debug("Map集合清除一个不存在的Key会报错吗？");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.remove("1");
        log.debug("结果是：不会报错");

    }

}
