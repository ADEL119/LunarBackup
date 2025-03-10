package com.lunarTC.lunarBackup.databases.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;

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
