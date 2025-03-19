package com.lunarTC.lunarBackup.databases.mariaDB.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@Getter
public class MariaDBConfigLoader {

    private List<MariaDBDatabaseConfig> databases;

    public MariaDBConfigLoader() {
        loadDatabaseConfigurations();
    }

    private void loadDatabaseConfigurations() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File("src/main/resources/MariaDBDatabases.json");

            // Deserialize JSON directly into an array and convert it to a list
            MariaDBDatabaseConfig[] configArray = objectMapper.readValue(file, MariaDBDatabaseConfig[].class);
            this.databases = Arrays.asList(configArray);  // Convert array to list
        } catch (Exception e) {
            throw new RuntimeException("Failed to load MariaDB database configurations", e);
        }
    }

}
