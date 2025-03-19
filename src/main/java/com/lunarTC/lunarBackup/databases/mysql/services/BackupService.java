package com.lunarTC.lunarBackup.databases.mysql.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.lunarTC.lunarBackup.databases.utils.DatabaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lunarTC.lunarBackup.databases.mysql.Configs.MySQLConfigLoader;
import com.lunarTC.lunarBackup.databases.mysql.Configs.MySQLDatabaseConfig;

@Service
public class BackupService {

    @Autowired
    MySQLConfigLoader mysqlConfigLoader;


    public void DailyBackup() {
        List<MySQLDatabaseConfig> databases = mysqlConfigLoader.getDatabases();

        for (MySQLDatabaseConfig database : databases) {
            try {
                File backupsDir = new File(database.getBackupsLocation());
                if (!backupsDir.exists()) {
                    backupsDir.mkdirs();
                }

                File dailyBackupDir = new File(backupsDir, "Daily_" + database.getDatabaseName());
                if (!dailyBackupDir.exists()) {
                    dailyBackupDir.mkdirs();
                }

                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String backupFile = new File(dailyBackupDir, database.getDatabaseName() + "_backup_" + date + ".sql").getAbsolutePath();

                executeMySQLBackup(database, backupFile);
                System.out.println("Daily Backup completed for database: " + database.getDatabaseName());
            } catch (Exception e) {
                System.err.println("Daily Backup failed for database: " + database.getDatabaseName() + " - " + e.getMessage());
            }
        }
    }





    public void WeeklyBackup() {
        List<MySQLDatabaseConfig> databases = mysqlConfigLoader.getDatabases();
        LocalDate today = LocalDate.now();

        // Format current date as "dd-MM-yyyy"
        String currentDate = today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        for (MySQLDatabaseConfig database : databases) {
            try {
                File backupsDir = new File(database.getBackupsLocation());
                if (!backupsDir.exists()) {
                    backupsDir.mkdirs();
                }

                File weeklyBackupDir = new File(backupsDir, "Weekly_" + database.getDatabaseName());
                if (!weeklyBackupDir.exists()) {
                    weeklyBackupDir.mkdirs();
                }

                // New backup file name format: "database_backup_15-03-2025.sql"
                String backupFile = new File(weeklyBackupDir,
                        database.getDatabaseName() + "_backup_" + currentDate + ".sql").getAbsolutePath();

                executeMySQLBackup(database, backupFile);
                System.out.println("Weekly Backup completed for database: " + database.getDatabaseName() + " (Date: " + currentDate + ")");
            } catch (Exception e) {
                System.err.println("Weekly Backup failed for database: " + database.getDatabaseName() + " - " + e.getMessage());
            }
        }
    }



    public void MonthlyBackup() {
        List<MySQLDatabaseConfig> databases = mysqlConfigLoader.getDatabases();
        String month = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));

        for (MySQLDatabaseConfig database : databases) {
            try {
                File backupsDir = new File(database.getBackupsLocation());
                if (!backupsDir.exists()) {
                    backupsDir.mkdirs();
                }

                File monthlyBackupDir = new File(backupsDir, "Monthly_" + database.getDatabaseName());
                if (!monthlyBackupDir.exists()) {
                    monthlyBackupDir.mkdirs();
                }

                String backupFile = new File(monthlyBackupDir, database.getDatabaseName() + "_backup_" + month + ".sql").getAbsolutePath();

                executeMySQLBackup(database, backupFile);
                System.out.println("Monthly Backup completed for database: " + database.getDatabaseName());
            } catch (Exception e) {
                System.err.println("Monthly Backup failed for database: " + database.getDatabaseName() + " - " + e.getMessage());
            }
        }
    }

    public void executeMySQLBackup(MySQLDatabaseConfig mySQLDatabaseConfig, String backupFile) throws Exception {
        String MYSQLDUMP_PATH = DatabaseUtils.getDumpPath("mysqldump");

        ProcessBuilder processBuilder = new ProcessBuilder(
                MYSQLDUMP_PATH,
                "-h", mySQLDatabaseConfig.getDatabaseHost(),
                "-P", mySQLDatabaseConfig.getDatabasePort(),
                "-u", mySQLDatabaseConfig.getMysqlUsername(),
                "-p" + mySQLDatabaseConfig.getMysqlPassword(), // Handle password safely
                mySQLDatabaseConfig.getDatabaseName()
        );

        processBuilder.redirectOutput(new File(backupFile));
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            boolean finished = process.waitFor(10, TimeUnit.MINUTES);

            if (!finished || process.exitValue() != 0) {
                throw new IOException("Backup failed with exit code " + process.exitValue());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Backup interrupted", e);
        }
    }
}
