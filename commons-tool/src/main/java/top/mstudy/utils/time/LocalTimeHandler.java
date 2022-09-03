package top.mstudy.utils.time;

/**
 * @author machao
 * @description:
 * @date 2022-09-02
 */
public class LocalTimeHandler implements ITimeHandler {

    @Override
    public long currentTimeMillis(boolean isLocal) throws Exception {
        return System.currentTimeMillis();
    }

}
