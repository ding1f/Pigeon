package com.pigeon.manager;

import com.pigeon.interfaces.RedisLockInterface;
import com.pigeon.interfaces.RedisLockManualAckInterface;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Redis锁管理类
 *
 * @author Idenn
 * @date 3/6/2024 2:34 PM
 */
@Component
public class RedisLockManager {

    /**
     * 分布式锁注入
     */
    @Resource
    RedissonClient redissonClient;

    /**
     * 读锁
     *
     * @param lockKey lockKey
     * @param after   after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:35 PM
     */
    public <T> boolean readLock(String lockKey, RedisLockInterface<? super T> after) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock = lock.readLock();
        return this.lockAction(rLock, after);
    }

    /**
     * 读锁 手动释放
     *
     * @param lockKey lockKey
     * @param after   after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:36 PM
     */
    public boolean readLockManual(String lockKey, RedisLockManualAckInterface after) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock = lock.readLock();
        return this.lockActionManual(rLock, after);
    }

    /**
     * 写锁
     *
     * @param lockKey lockKey
     * @param after   after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:36 PM
     */
    public <T> boolean writeLock(String lockKey, RedisLockInterface<? super T> after) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock = lock.writeLock();
        return this.lockAction(rLock, after);
    }

    /**
     * 写锁 手动释放
     *
     * @param lockKey lockKey
     * @param after   after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:36 PM
     */
    public <T> boolean writeLockManual(String lockKey, RedisLockManualAckInterface after) {
        RReadWriteLock lock = redissonClient.getReadWriteLock(lockKey);
        RLock rLock = lock.writeLock();
        return this.lockActionManual(rLock, after);
    }

    /**
     * 实现Redis分布式锁
     *
     * @param rLock rLock
     * @param after after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:37 PM
     */
    private <T> boolean lockAction(RLock rLock, RedisLockInterface<? super T> after) {
        boolean flag = false;
        try {
            if (rLock.tryLock()) {
                after.action();
                flag = true;
            }
        } catch (Exception ex) {
            return false;
        } finally {
            if (flag) {
                rLock.unlock();  // 释放写锁
            }
        }
        return flag;
    }

    /**
     * redis分布式锁实现(手动释放)
     *
     * @param rLock rLock
     * @param after after
     * @return boolean
     * @author Idenn
     * @date 3/6/2024 2:37 PM
     */
    private boolean lockActionManual(RLock rLock, RedisLockManualAckInterface after) {
        boolean flag = false;
        try {
            if (rLock.tryLock()) {
                after.action(rLock);
                flag = true;
            }
        } catch (Exception ex) {
            return false;
        }
        return flag;
    }
}
