package com.lunarTC.lunarBackup.databases.mariaDB.configs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Ignore null fields during serialization
public class MariaDBDatabaseConfig {

    @JsonProperty("configName")
    private String configName;            // Name of the database configuration (e.g., "ProductionDB")

    @JsonProperty("databaseHost")
    private String databaseHost;          // MariaDB Server Host (e.g., "localhost" or "192.168.1.50")

    @JsonProperty("databasePort")
    private int databasePort = 3306;      // MariaDB Port (default: 3306)

    @JsonProperty("databaseName")
    private String databaseName;          // The database to back up or restore

    @JsonProperty("mariaDBUsername")
    private String mariaDBUsername;       // MariaDB user with backup/restore privileges

    @JsonProperty("mariaDBPassword")
    private String mariaDBPassword;       // Password for the MariaDB user

    @JsonProperty("backupsLocation")
    private String backupsLocation;       // Directory where backup files will be saved

    @JsonProperty("backupCommandOptions")
    private String backupCommandOptions = "";  // User can leave empty

    @JsonProperty("restoreCommandOptions")
    private String restoreCommandOptions = ""; // User can leave empty
}
