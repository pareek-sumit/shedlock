package com.example.history_migration.service;

import com.example.history_migration.config.LockManager;
import jakarta.annotation.PostConstruct;
import net.javacrumbs.shedlock.core.SimpleLock;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IdService idService;

    private static final int BATCH_SIZE = 100;

    private final LockManager lockManager;
    private Integer minId;
    private Integer maxId;

    public TrackingService(LockManager lockManager) {
        this.lockManager = lockManager;
    }

    @PostConstruct
    public void init() {
        this.minId = idService.getMinId();
        this.maxId = idService.getMaxId();
    }

    @Scheduled(fixedRate = 600000) // Run every minute, adjust as needed
    //@SchedulerLock(name = "tracking_task", lockAtMostFor = "PT5M", lockAtLeastFor = "PT4M")
    public void processRecords() throws InterruptedException {
        // Fetch cached min and max IDs

        if (minId == null || maxId == null) {
            return;
        }

        // Fetch the last tracking entry to determine the next batch start
        Long lastToId = null;
        try {
            lastToId = jdbcTemplate.queryForObject(
                    "SELECT to_id FROM Tracking ORDER BY to_id DESC LIMIT 1",
                    Long.class);
        } catch (EmptyResultDataAccessException e) {
            // Handle the case where no rows are returned
            //lastToId =  null; // or some default value
        }

        long startId = minId;
        long endId = Math.min(startId + BATCH_SIZE - 1, maxId);

        if (lastToId != null) {
            startId = lastToId + 1;
            endId = Math.min(startId + BATCH_SIZE - 1, maxId);
        }

        if (startId <= maxId) {

            String lockName = "process_records_" + startId + "_to_" + endId;

            Optional<SimpleLock> lock = lockManager.acquireLock(lockName);

            if (lock.isPresent()) {
                try {

                    // Fetch records to process
                    List<Integer> ids = jdbcTemplate.queryForList(
                            "SELECT id FROM My_entity WHERE id BETWEEN ? AND ?", Integer.class, startId, endId
                    );

                    // Insert tracking entry for the current batch
                    jdbcTemplate.update(
                            "INSERT INTO Tracking (from_id, to_id, status) VALUES (?, ?, ?)",
                            startId, endId, "New"
                    );

                    // Process each record (dummy processing here)
                    //for (Integer id : ids) {
                    Thread.sleep(180000);
                    // }

                    // Update tracking status
                    jdbcTemplate.update(
                            "UPDATE Tracking SET status = ? WHERE from_id = ? AND to_id = ?",
                            "Success", startId, endId
                    );
                } finally {
                    lock.get().unlock(); // Release the lock
                }
            } else {
                // Lock not acquired, handle the case (e.g., retry later)
            }
        }
    }
}