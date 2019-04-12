package cn.farwalker.waka.util;

import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 线程工具类
 *
 * @author Administrator
 */
public class ThreadUtil {
    private static final Logger log = LoggerFactory.getLogger(ThreadUtil.class);
    /** 线程池数量(有些线程需要等待时,就会占用一个位置) */
    public static final int POOL_SIZE = 15;
    //private static final  ExecutorService pools = Executors.newFixedThreadPool(POOL_SIZE);
    private static final ScheduledExecutorService pools = Executors.newScheduledThreadPool(POOL_SIZE);

    protected static final ThreadUtil util = new ThreadUtil();
    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    private ThreadUtil() {
        //TimedTaskManger.startTask();//启动定时任务
    }

    /**
     * 功能: 等待一段时间
     *
     * @param time 毫秒
     * @author 2007-1-5 21:11:29
     */
    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("功能: 等待一段时间", e);
        }
    }

    /** 使用线程池执行(有监控过长的运行时间)
     public Future<?> run(Runnable task) {
     return run(task,null);
     }*/
    /** 
     * 使用线程池执行(有监控过长的运行时间)
     * @param task
     * @param threadName 
     * @return
     */
    public Future<?> runSingle(Runnable task, String threadName) {
        /*Runnable pr = new PoolRunnable(task, threadName);
        Future<?> rs = pools.submit(pr);
        return rs;*/
        return runSingle(task, threadName, 0);
    }
    
    /**
     * 单次运行
     * @param task
     * @param threadName
     * @param delay 延迟分钟,0:马上执行
     * @return
     */
    public Future<?> runSingle(Runnable task, String threadName,int delay) {        
        Runnable pr = new PoolRunnable(task, threadName);
        Future<?> rs ;
        if(delay<=0){
            rs = pools.submit(pr);
        }
        else{
            rs = pools.schedule(pr, delay, TimeUnit.MINUTES);
        }
        return rs;
    }
    /**
     * 定时执行任务,执行多次<br/>
     * (第一次执行是立即执行；如果这个定时任务没有执行完，就算超过了指定时间，也要等前一次任务执行完后再执行--等下次够时间再执行)
     *
     * @param task       任务
     * @param threadName
     * @param minutes    分钟,第一次执行是立即执行,然后按minutes间隔执行
     * @return
     */
    public Future<?> runMulti(Runnable task, String threadName, int minutes) {
        if (minutes <= 0) {
            return runSingle(task, threadName,0);
        }

        Runnable pr = new PoolRunnable(task, threadName);
        Future<?> rs = pools.scheduleWithFixedDelay(pr, 0, minutes, TimeUnit.MINUTES);
        //Future<?> rs = pools.submit(pr);
        return rs;
    }
     
    /** 使用线程池执行 
    public <T> Future<T> run(Callable<T> task, String threadName) {
        Future<T> rs = pools.submit(task);
        return rs;
    }*/

    private static int threadCount = 0;

    private class PoolRunnable implements Runnable {
        private Runnable task;
        private StringBuilder name;

        public PoolRunnable(Runnable run, String threadName) {
            this.task = run;
            Calendar c = Calendar.getInstance();
            name = new StringBuilder();
            if (Tools.string.isEmpty(threadName)) {
                threadName = "threadUtil";
            }
            name.append(threadName).append(++threadCount).append('_')
                    .append(Tools.date.formatDate(c, DateUtil.FORMAT.YYYYMMDDHHMMSS));
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();// Tools.date.formatDate();
            try {
                task.run();
            } finally {
                long endTime = System.currentTimeMillis();//Tools.date.formatDate();
                long runtime = (endTime - startTime) / 1000;
                if (runtime > 10) {
                    log.error(name.toString() + "运行时间过长(秒):" + runtime);
                }
            }
        }
    }

    public static int getThreadCount() {
        return threadCount;
    }
    
    public void shutdown(){
        pools.shutdownNow();
        pools.shutdown();
    }
}
