package com.ticketing.ticket.lock;

import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.exception.TicketErrorCode;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisDistributedLockExecutor implements DistributedLockExecutor {

    private final RedissonClient redissonClient;

    public RedisDistributedLockExecutor(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public <T> T executeWithLock(String lockKey, Duration waitTime, Duration leaseTime, Supplier<T> task) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(waitTime.toMillis(), leaseTime.toMillis(), TimeUnit.MILLISECONDS);
            if (!locked) {
                throw new BusinessException(TicketErrorCode.SEAT_LOCK_ACQUISITION_FAILED, lockKey);
            }
            return task.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(TicketErrorCode.SEAT_LOCK_ACQUISITION_FAILED, "interrupted");
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
