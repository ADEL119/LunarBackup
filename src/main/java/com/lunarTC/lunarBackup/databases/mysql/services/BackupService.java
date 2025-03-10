package com.lunarTC.lunarBackup.databases.mysql.services;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    @Scheduled(cron = "0 */1 * * * ?")
    public void DailyBackup() {
        List<MySQLDatabaseConfig> databases = mysqlConfigLoader.getDatabases(); // Get database list dynamically

        for (MySQLDatabaseConfig database : databases) {
            try {
                // Ensure backupsLocation exists
                         File backupsDir = new File(database.getBackupsLocation());
                if (!backupsDir.exists()) {
                    backupsDir.mkdirs();
                }

                // Create daily backup folder with database name
                File dailyBackupDir = new File(backupsDir, "dailyBackup_" + database.getDatabaseName());
                if (!dailyBackupDir.exists()) {
                    dailyBackupDir.mkdirs();
                }

                // Generate backup file path with date format (dd-MM-yyyy)
               /* String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                String backupFile = new File(dailyBackupDir, database.getDatabaseName() + "_backup_" + date + ".sql").getAbsolutePath();*/

                String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
                String backupFile = new File(dailyBackupDir, database.getDatabaseName() + "_backup_" + dateTime + ".sql").getAbsolutePath();


                // Execute backup
                executeMySQLBackup(database, backupFile);
                System.out.println("Backup completed successfully for database: " + database.getDatabaseName());
            } catch (Exception e) {
                System.err.println("Backup failed for database: " + database.getDatabaseName() + " - " + e.getMessage());
            }
        }
    }

    public void WeeklyBackup() {

    }

    public void MonthlyBackup() {

    }

    public void executeMySQLBackup(MySQLDatabaseConfig mySQLDatabaseConfig, String backupFile) throws Exception {
        // Getting the path to mysqldump using a utility method
        String MYSQLDUMP_PATH = DatabaseUtils.getDumpPath("mysqldump");
        System.out.println(MYSQLDUMP_PATH);

        // Constructing the backup command
        StringBuilder commandBuilder = new StringBuilder();
        commandBuilder.append(MYSQLDUMP_PATH)
                .append(" -h").append(mySQLDatabaseConfig.getDatabaseHost())    // Host (localhost or remote IP)
                .append(" -P").append(mySQLDatabaseConfig.getDatabasePort())    // Port (default: 3306)
                .append(" -u").append(mySQLDatabaseConfig.getMysqlUsername())   // MySQL username
                .append(" -p").append(mySQLDatabaseConfig.getMysqlPassword())   // MySQL password
                .append(" ").append(mySQLDatabaseConfig.getDatabaseName());     // Database name

        // Always append the backupCommandOptions (even if empty)
        commandBuilder.append(" ").append(mySQLDatabaseConfig.getBackupCommandOptions());

        // Create the command string
        String command = commandBuilder.toString();

        // Using ProcessBuilder to execute the command
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.redirectOutput(new File(backupFile)); // Redirect output to back up file
        processBuilder.redirectErrorStream(true); // Merge error stream with output stream

        try {
            Process process = processBuilder.start();
            boolean finished = process.waitFor(10, TimeUnit.MINUTES); // Wait for the process to complete

            if (!finished || process.exitValue() != 0) {
                throw new IOException("Backup failed with exit code " + process.exitValue());
            }
        } catch (InterruptedException e) {
            // Handle case where the backup process was interrupted
            Thread.currentThread().interrupt();
            throw new IOException("Backup interrupted", e);
        }
    }

}
