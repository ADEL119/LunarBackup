package com.lunarTC.lunarBackup.databases.mysql.Configs;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MySQLConfigLoader {

    private List<MySQLDatabaseConfig> databases;

    public MySQLConfigLoader() {
        loadDatabaseConfigurations();
    }

    private void loadDatabaseConfigurations() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/main/resources/MySQLDatabases.json");

            // Deserialize JSON directly into an array and convert it to a list
            MySQLDatabaseConfig[] configArray = objectMapper.readValue(file, MySQLDatabaseConfig[].class);
            this.databases = Arrays.asList(configArray);  // Convert array to list
        } catch (Exception e) {
            throw new RuntimeException("Failed to load MySQL database configurations", e);
        }
    }

    public List<MySQLDatabaseConfig> getDatabases() {
        return databases;
    }
}

