package com.lunarTC.lunarBackup.databases.mysql.controllers;

import com.lunarTC.lunarBackup.databases.mysql.services.BackupSchedulerService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backup-scheduler")
public class BackupSchedulerController {

    @Autowired
    private BackupSchedulerService backupSchedulerService;

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleBackup(
            @RequestParam String jobName,
            @RequestParam String group,
            @RequestParam String cronExpression,
            @RequestParam String backupType) throws SchedulerException {

        if (!isValidBackupType(backupType)) {
            return ResponseEntity.badRequest().body("Invalid backup type: " + backupType);
        }

        backupSchedulerService.scheduleBackup(jobName, group, cronExpression, backupType);
        return ResponseEntity.ok("Backup job scheduled successfully: " + jobName);
    }

    @DeleteMapping("/unschedule")
    public ResponseEntity<String> unscheduleBackup(
            @RequestParam String jobName,
            @RequestParam String group) throws SchedulerException {

        backupSchedulerService.unscheduleBackup(jobName, group);
        return ResponseEntity.ok("Backup job unscheduled successfully: " + jobName);
    }

    @PostMapping("/pause")
    public ResponseEntity<String> pauseBackup(
            @RequestParam String jobName,
            @RequestParam String group) throws SchedulerException {

        backupSchedulerService.pauseBackup(jobName, group);
        return ResponseEntity.ok("Backup job paused successfully: " + jobName);
    }

    @PostMapping("/resume")
    public ResponseEntity<String> resumeBackup(
            @RequestParam String jobName,
            @RequestParam String group) throws SchedulerException {

        backupSchedulerService.resumeBackup(jobName, group);
        return ResponseEntity.ok("Backup job resumed successfully: " + jobName);
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> isJobScheduled(
            @RequestParam String jobName,
            @RequestParam String group) throws SchedulerException {

        boolean exists = backupSchedulerService.isJobScheduled(jobName, group);
        return ResponseEntity.ok(exists);
    }

    private boolean isValidBackupType(String backupType) {
        return backupType.equalsIgnoreCase("daily") ||
                backupType.equalsIgnoreCase("weekly") ||
                backupType.equalsIgnoreCase("monthly");
    }
}
