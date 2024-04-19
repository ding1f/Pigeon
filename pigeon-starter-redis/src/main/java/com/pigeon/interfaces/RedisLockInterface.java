package com.pigeon.interfaces;

/**
 * redis分布式锁函数式接口
 *
 * @author Idenn
 * @date 3/5/2024 9:47 PM
 */
@FunctionalInterface
public interface RedisLockInterface<T> {

    /**
     * 函数式方法潜入
     * @param
     * @return void
     * @author Idenn
     * @date 3/6/2024 2:32 PM
     */
    void action() throws InterruptedException;
}
