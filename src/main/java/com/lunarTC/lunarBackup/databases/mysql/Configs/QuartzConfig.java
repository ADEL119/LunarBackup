package com.lunarTC.lunarBackup.databases.mysql.Configs;

import com.lunarTC.lunarBackup.databases.mysql.services.BackupJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail dailyBackupJobDetail() {
        return JobBuilder.newJob(BackupJob.class)
                .withIdentity("DailyBackupJob", "BackupGroup")
                .usingJobData("backupType", "daily") // Pass backup type
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger dailyBackupTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(dailyBackupJobDetail())
                .withIdentity("DailyBackupTrigger", "BackupGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")) // Every minute
                .build();
    }
    @Bean
    public JobDetail weeklyBackupJobDetail() {
        return JobBuilder.newJob(BackupJob.class)
                .withIdentity("WeeklyBackupJob", "BackupGroup")
                .usingJobData("backupType", "weekly") // Pass backup type
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weeklyBackupTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(weeklyBackupJobDetail())
                .withIdentity("WeeklyBackupTrigger", "BackupGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */2 * * * ?")) // Every Saturday at 2 AM
                .build();
    }

    @Bean
    public JobDetail monthlyBackupJobDetail() {
        return JobBuilder.newJob(BackupJob.class)
                .withIdentity("MonthlyBackupJob", "BackupGroup")
                .usingJobData("backupType", "monthly") // Pass backup type
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger monthlyBackupTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(monthlyBackupJobDetail())
                .withIdentity("MonthlyBackupTrigger", "BackupGroup")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */3 * * * ?")) // 1st day of every month at 3 AM
                .build();
    }
}