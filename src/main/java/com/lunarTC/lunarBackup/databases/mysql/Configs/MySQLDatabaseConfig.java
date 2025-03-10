package com.lunarTC.lunarBackup.databases.mysql.Configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore null fields during serialization
public class MySQLDatabaseConfig {

    @JsonProperty("configName")
    private String configName;            // Name of the database configuration (e.g., "ProductionDB")

    @JsonProperty("databaseHost")
    private String databaseHost;          // MySQL Server Host (e.g., "localhost" or "192.168.1.50")

    @JsonProperty("databasePort")
    private int databasePort = 3306;      // MySQL Port (default: 3306)

    @JsonProperty("databaseName")
    private String databaseName;          // The database to back up or restore

    @JsonProperty("mysqlUsername")
    private String mysqlUsername;         // MySQL user with backup/restore privileges

    @JsonProperty("mysqlPassword")
    private String mysqlPassword;         // Password for the MySQL user

    @JsonProperty("backupsLocation")
    private String backupsLocation;       // Directory where backup files will be saved

    @JsonProperty("backupCommandOptions")
    private String backupCommandOptions = "";  // User can leave empty

    @JsonProperty("restoreCommandOptions")
    private String restoreCommandOptions = ""; // User can leave empty
}
