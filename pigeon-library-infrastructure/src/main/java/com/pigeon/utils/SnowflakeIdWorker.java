package com.pigeon.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 通过雪花算法生成唯一Id
 *
 * @author Idenn
 * @date 3/5/2024 1:32 PM
 */
@Component
public class SnowflakeIdWorker {
    // 工作机器ID(0~31)
    @Value("${pigeon.system.worker-id:0}")
    private long workerId;
    // 数据中心ID(0~31)
    @Value("${pigeon.system.datacenter-id:0}")
    private long datacenterId;
    // 毫秒内序列(0~4095)
    private long sequence = 0L;
    // 时间戳起始点，一般取系统的最近时间（一旦定义不能更改）
    private long twepoch = 1288834974657L;
    // 工作机器ID位数
    private long workerIdBits = 5L;
    // 数据中心ID位数
    private long datacenterIdBits = 5L;
    // 序列在id中占的位数
    private long sequenceBits = 12L;
    // 工作机器ID最大值
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 数据中心ID最大值
    private long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 生成序列的掩码
    private long sequenceMask = -1L ^ (-1L << sequenceBits);
    // 工作机器ID向左移位数
    private long workerIdShift = sequenceBits;
    // 数据中心ID向左移位数
    private long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间戳向左移位数
    private long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;

    public SnowflakeIdWorker() {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
    }

    public synchronized String nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return String.valueOf(((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence);
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
