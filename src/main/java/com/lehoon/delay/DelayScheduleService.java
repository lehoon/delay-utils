package com.lehoon.delay;

import com.lehoon.delay.task.AbstractDelayedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Title: DelayScheduleService
 * @ProjectName: delayutils
 * @Description: 延迟度列调度服务
 * @Author: lehoon
 * @Date: 2018/10/25 13:08
 **/
public final class DelayScheduleService {

    /**
     * 工作线程池
     */
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 延迟队列
     */
    private DelayQueue<AbstractDelayedTask> delayedTaskDelayQueue = new DelayQueue<AbstractDelayedTask>();
    private volatile boolean running = false;
    private Thread scheduleThread = null;
    private static Logger logger = LoggerFactory.getLogger(DelayScheduleService.class);

    /**
     * 初始化方法
     */
    public void init() {
        if(running) {
            logger.warn("延迟调度任务处理已经启动,不能重复启动。");
            return;
        }

        running = true;
        scheduleThread = new Thread(new ScheduleTask());
        scheduleThread.setName("DelayScheduleTaskServiceThread");
        scheduleThread.setDaemon(true);
        scheduleThread.start();

        logger.info("延迟调度任务已经启动.");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
    }

    public void shutdown() {
        if(!running) {
            logger.warn("延迟调度任务处理未启动,不能停止。");
            return;
        }

        running = false;

        if(scheduleThread != null && scheduleThread.isAlive()) {
            scheduleThread.interrupt();
        }
    }

    /**
     * 提交一个延迟任务
     * @param delayedTask
     */
    public void put(AbstractDelayedTask delayedTask) {
        delayedTaskDelayQueue.add(delayedTask);
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    /**
     * 调度延迟任务并执行
     */
    final class ScheduleTask implements Runnable {
        Logger logger = LoggerFactory.getLogger(ScheduleTask.class);
        @Override
        public void run() {
            AbstractDelayedTask task = null;
            while (running) {
                try{
                    //获取延迟任务
                    task = delayedTaskDelayQueue.poll(1, TimeUnit.SECONDS);

                    if(task != null) {
                        logger.info("开始执行延迟任务, 任务信息:{}", task);
                        threadPoolExecutor.submit(task);
                    }
                } catch (InterruptedException e) {
                    logger.error("延迟调度线程异常:{}, 任务信息:{}", e, task);
                }
            }
        }
    }
}
