package com.lunarTC.lunarBackup.databases.utils;

import com.lunarTC.lunarBackup.databases.mysql.services.BackupService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BackupJob implements Job {

    @Autowired
    private BackupService backupService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Get the backup type from JobDataMap
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String backupType = jobDataMap.getString("backupType");

        // Execute the appropriate backup based on the type
        switch (backupType) {
            case "daily":
                backupService.DailyBackup();
                break;
            case "weekly":
                backupService.WeeklyBackup();
                break;
            case "monthly":
                backupService.MonthlyBackup();
                break;
            default:
                throw new JobExecutionException("Invalid backup type: " + backupType);
        }
    }
}