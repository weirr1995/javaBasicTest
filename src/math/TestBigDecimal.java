package math;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author linjing
 * @date: Created in 2020/7/6
 */
@Slf4j
public class TestBigDecimal {

    @Test
    public void  testPrecision(){
        BigDecimal bigDecimal1 = new BigDecimal("1");
        BigDecimal bigDecimal2 = new BigDecimal("1.2343");
        BigDecimal bigDecimal3 = new BigDecimal("0.2343");
        log.debug("该数值的精度为：{}",bigDecimal1.precision());
        log.debug("该数值的精度为：{}",bigDecimal2.precision());
        log.debug("该数值的精度为：{}",bigDecimal3.precision());
    }

}
