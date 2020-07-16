package math;

import org.junit.Test;

import java.util.Random;

/**
 * @author linjing
 * @date: Created in 2020/7/16
 */
public class RandomLongWithRange {
    @Test
    public void test(){
        Random random = new Random();
        System.out.println(nextLong(random,5000));
    }

    public static  long nextLong(Random rng, long n) {
        // error checking and 2^x checking removed for simplicity.
        long bits, val;
        do {
            bits = (rng.nextLong() << 1) >>> 1;
            val = bits % n;
        } while (bits-val+(n-1) < 0L);
        return val;
    }

}
