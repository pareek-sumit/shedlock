package com.example.history_migration;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableSchedulerLock(defaultLockAtLeastFor = "PT4M", defaultLockAtMostFor = "PT5M")
@SpringBootApplication
public class HistoryMigrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(HistoryMigrationApplication.class, args);
	}

}
