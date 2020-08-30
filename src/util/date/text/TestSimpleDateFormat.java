package util.date.text;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author linjing
 * @date: Created in 2020/8/13
 */
@Slf4j
public class TestSimpleDateFormat {
    @Test
    public void test(){
        //24小时制
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("{}",sdf1.format(new Date()));
        //12小时制
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        log.info("{}",sdf2.format(new Date()));

    }
}
