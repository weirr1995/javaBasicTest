package concurrent.thread;

/**
 * @author linjing
 * @date: Created in 2020/6/23
 */
public class Yield {
    /**
     * 会使的线程放弃当前分得的cpu时间片，但此时线程任然处于可执行状态，随时可以再次分得cpu时间片。yield()方法只能使同优先级的线程有执行的机会。调用 yield()的效果等价于调度程序认为该线程已执行了足够的时间从而转到另一个线程。(暂停当前正在执行的线程，并执行其他线程，且让出的时间不可知)
     */
}
