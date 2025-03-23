package com.lunarTC.lunarBackup.databases.utils;

import com.lunarTC.lunarBackup.databases.mysql.services.BackupService;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DatabaseUtils {


    public static String getDumpPath(String dumpCommand) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        String command = os.contains("win") ? "where " + dumpCommand : "which " + dumpCommand;

        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line = reader.readLine();
            if (line != null) {
                return line.trim();
            } else {
                throw new Exception(dumpCommand + " not found");
            }
        }
    }


}
