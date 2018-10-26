package com.lehoon.delay;

import java.util.concurrent.*;

/**
 * @Title: FixThreadPoolExecutor
 * @ProjectName: delayutils
 * @Description: 固定大小的线程池
 * @Author: lehoon
 * @Date: 2018/10/25 13:32
 **/
public class FixThreadPoolExecutor extends ThreadPoolExecutor {
    public FixThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());
    }
}
