package cn.farwalker.standard.core.temp;

/**
 * Created by asus on 2019/3/28.
 */

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogManager {
    private final int OPERATE_DELAY_TIME = 10;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
    public static LogManager logManager = new LogManager();

    private LogManager() {
    }

    public static LogManager me() {
        return logManager;
    }

    public void executeLog(TimerTask task) {
        this.executor.schedule(task, 10L, TimeUnit.MILLISECONDS);
    }
}