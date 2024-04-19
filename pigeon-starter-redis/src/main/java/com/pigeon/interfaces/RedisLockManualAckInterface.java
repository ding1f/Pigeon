package com.pigeon.interfaces;

import org.redisson.api.RLock;

/**
 * redis分布式锁函数式接口 手动释放锁
 *
 * @author Idenn
 * @date 3/5/2024 9:47 PM
 */
@FunctionalInterface
public interface RedisLockManualAckInterface<T> {
    void action(RLock rLock);
}
