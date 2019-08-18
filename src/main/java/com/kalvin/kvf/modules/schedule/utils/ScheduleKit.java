package com.kalvin.kvf.modules.schedule.utils;

import com.kalvin.kvf.common.exception.KvfException;
import com.kalvin.kvf.common.utils.SpringContextKit;
import com.kalvin.kvf.modules.schedule.constant.JobConstant;
import com.kalvin.kvf.modules.schedule.entity.Job;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务工具类
 * Create by Kalvin on 2019/8/17
 */
public class ScheduleKit {

    private final static String JOB_KEY = "JOB_";

    private final static Scheduler scheduler = SpringContextKit.getBean(Scheduler.class);

    public static void create(Long jobId, Job job) {
        try {
            // 启动调度器
//            scheduler.start();
            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(ScheduleKit.getJobClass(job.getBean()).getClass()).withIdentity(getJobKey(jobId)).build();
            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId))
                    .withSchedule(scheduleBuilder).build();
            // 传入参数
            jobDetail.getJobDataMap().put(JobConstant.JOB_MAP_KEY, job.getParams());
            scheduler.scheduleJob(jobDetail, trigger);

            // 默认创建时任务设置为暂停
            ScheduleKit.pause(jobId);
        } catch (SchedulerException e) {
            throw new KvfException("创建定时任务失败");
        }
    }

    public static void pause(Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new KvfException("暂停定时任务失败");
        }
    }

    public static void resume(Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new KvfException("恢复启动定时任务失败");
        }
    }

    public static void update(Long jobId, Job job) {
        try {
            TriggerKey triggerKey = getTriggerKey(jobId);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            throw new KvfException("更新定时任务失败");
        }
    }

    public static void delete(Long jobId) {
        try {
            scheduler.pauseTrigger(getTriggerKey(jobId));
            scheduler.unscheduleJob(getTriggerKey(jobId));
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new KvfException("删除定时任务失败");
        }
    }

    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_KEY + jobId);
    }

    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_KEY + jobId);
    }

    public static QuartzJobBean getJobClass(String classname) {
        return (QuartzJobBean) SpringContextKit.getBean(classname);
    }
}
