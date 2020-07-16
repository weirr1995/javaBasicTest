package concurrent.thread;

/**
 * @author linjing
 * @date: Created in 2020/7/16
 * 成功解决哲学家吃饭问题的方法：
 * 1、允许最多 4 个哲学家同时坐在桌子上。
 * 2、只有一个哲学家的两根筷子都可用时，他才能拿起它们（他必须在临界区内拿起两根 辕子)。
 * 3、使用非对称解决方案。即单号的哲学家先拿起左边的筷子，接着右边的筷子；而双 号的哲学家先拿起右边的筷子，接着左边的筷子。
 *
 * 这里我们用第2中解决方法实现
 */

public class TestPhilosophersEat_Success {
}
