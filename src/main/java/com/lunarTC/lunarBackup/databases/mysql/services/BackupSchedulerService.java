package com.lunarTC.lunarBackup.databases.mysql.services;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BackupSchedulerService {

    @Autowired
    private Scheduler scheduler;

    /**
     * Schedules a backup job with a given cron expression.
     */
    public void scheduleBackup(String jobName, String group, String cronExpression, String backupType) throws SchedulerException {
        // Create JobDataMap to pass the backup type
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("backupType", backupType);

        // Check if job already exists
        JobKey jobKey = new JobKey(jobName, group);
        if (scheduler.checkExists(jobKey)) {
            throw new SchedulerException("Job with name " + jobName + " already exists.");
        }

        // Define the job
        JobDetail jobDetail = JobBuilder.newJob(BackupJob.class)
                .withIdentity(jobKey)
                .usingJobData(jobDataMap) // Pass the backup type
                .storeDurably()
                .build();

        // Define the trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobName + "Trigger", group)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        // Schedule the job
        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * Deletes (unschedules) a job.
     */
    public void unscheduleBackup(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        } else {
            throw new SchedulerException("Job with name " + jobName + " does not exist.");
        }
    }

    /**
     * Pauses a scheduled job.
     */
    public void pauseBackup(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        if (scheduler.checkExists(jobKey)) {
            scheduler.pauseJob(jobKey);
        } else {
            throw new SchedulerException("Job with name " + jobName + " does not exist.");
        }
    }

    /**
     * Resumes a paused job.
     */
    public void resumeBackup(String jobName, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, group);
        if (scheduler.checkExists(jobKey)) {
            scheduler.resumeJob(jobKey);
        } else {
            throw new SchedulerException("Job with name " + jobName + " does not exist.");
        }
    }

    /**
     * Checks if a job exists.
     */
    public boolean isJobScheduled(String jobName, String group) throws SchedulerException {
        return scheduler.checkExists(new JobKey(jobName, group));
    }
}
