package cn.farwalker.ravv.service.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.core.ITimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import static org.quartz.DateBuilder.nextGivenSecondDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by dkzhang on 16/8/3.
 */
@Slf4j
@Component
public class JobSchedulerFactory {


    private static Scheduler scheduler;

    @Autowired
    public void setScheduler(Scheduler scheduler){
        JobSchedulerFactory.scheduler = scheduler;
    }

    public static SimpleTrigger getSimpleTrigger(
            String triggerId, String group, int startTimeInSeconds,
            int repeatCount, int repeatIntervalInSeconds) throws SchedulerException {
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity(triggerId, group)
                .startAt(nextGivenSecondDate(null, startTimeInSeconds))
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(repeatIntervalInSeconds)
                        .withRepeatCount(repeatCount)
                )
                .build();
        return simpleTrigger;
    }


    public static SimpleTrigger getSimpleTrigger(
            String triggerId, String group, Date startTime,
            int repeatCount, int repeatIntervalInSeconds) throws SchedulerException {
        SimpleTrigger simpleTrigger = (SimpleTrigger) newTrigger()
                .withIdentity(triggerId, group)
                .startAt(startTime)
                .withSchedule(simpleSchedule()
                        .withIntervalInSeconds(repeatIntervalInSeconds)
                        .withRepeatCount(repeatCount)
                )
                .build();
        return simpleTrigger;
    }


    public static void callTaskJob(long delay, Class jobClass, String jobName, JobDataMap dataMap){
        try {
            String id = UUID.randomUUID().toString();
            JobDetail job = newJob(jobClass).withIdentity(jobName + ":" + id, jobName).build();
            job.getJobDataMap().putAll(dataMap);

            long nowTime = System.currentTimeMillis();
            nowTime += delay;
            Date startTime = new Date(nowTime);

            Date ft = scheduler.scheduleJob(job, getSimpleTrigger(jobName + ":" + id, jobName, startTime, 0, 0));
            log.info(job.getKey() + " will run at: " + ft + " and repeat 0  times ");
        }catch (SchedulerException se){
            se.printStackTrace();
        }
    }

}
