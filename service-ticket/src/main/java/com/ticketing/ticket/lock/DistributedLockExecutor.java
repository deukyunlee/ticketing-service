package com.ticketing.ticket.lock;

import java.time.Duration;
import java.util.function.Supplier;

public interface DistributedLockExecutor {
    <T> T executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Supplier<T> task);
}
