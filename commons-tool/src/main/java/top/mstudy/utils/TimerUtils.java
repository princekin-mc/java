package top.mstudy.utils;

/**
 * @author machao
 * @description: 计时器
 * @date 2022-09-02
 */
public class TimerUtils {
    private long start;

    public TimerUtils() {
        start = System.nanoTime();
    }

    public long getUseTime() {
        return System.nanoTime() - start;
    }

    public long getUseTimeInMillis() {
        return (System.nanoTime() - start) / 1000000;
    }


    public long reset() {
        long end = System.nanoTime();
        long use = end - start;
        start = end;
        return use;
    }

    public long resetInMillis() {
        return reset() / 1000000;
    }
}

