package com.example.history_migration.config;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class LockManager {

    private final LockProvider lockProvider;

    public LockManager(LockProvider lockProvider) {
        this.lockProvider = lockProvider;
    }

    public Optional<SimpleLock> acquireLock(String lockName) {
        LockConfiguration lockConfiguration = new LockConfiguration(
                Instant.now(),
                lockName,
                Duration.ofMinutes(5), // lockAtMostFor
                Duration.ofMillis(4)    // lockAtLeastFor (minimal, to avoid delay)
        );
        return lockProvider.lock(lockConfiguration);
    }
}
