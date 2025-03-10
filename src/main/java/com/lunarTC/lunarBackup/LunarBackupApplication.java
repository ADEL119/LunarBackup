package com.lunarTC.lunarBackup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class LunarBackupApplication {

	
	    public static void main(String[] args) {
	        SpringApplication.run(LunarBackupApplication.class, args);
	    }

	    

}
