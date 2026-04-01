package com.ticketing.ticket.lock;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DistributedLockAop {

    private static final String LOCK_PREFIX = "lock:";

    private final DistributedLockExecutor lockExecutor;

    @Around("@annotation(com.ticketing.ticket.lock.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String dynamicKey = CustomSpringELParser.getDynamicValue(joinPoint.getArgs(), method, distributedLock.key());
        String key = LOCK_PREFIX + dynamicKey;
        Duration waitTime = toDuration(distributedLock.waitTime(), distributedLock.timeUnit());
        Duration leaseTime = toDuration(distributedLock.leaseTime(), distributedLock.timeUnit());

        return lockExecutor.executeWithLock(key, waitTime, leaseTime, () -> proceed(joinPoint));
    }

    private Duration toDuration(long value, TimeUnit timeUnit) {
        return Duration.ofMillis(timeUnit.toMillis(value));
    }

    private Object proceed(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
