package com.lehoon.delay.task;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Title: AbstractDelayedTask
 * @ProjectName: delayutils
 * @Description: 任务抽象类
 * @Author: lehoon
 * @Date: 2018/10/25 13:08
 **/
public abstract class AbstractDelayedTask implements Delayed, Runnable {

    //默认30秒
    protected final static long DELAY = 30 * 1000L;
    private long startTime;
    private long expireTime;
    private Date createTime;
    private String taskId;

    public AbstractDelayedTask() {
    }

    public AbstractDelayedTask(String taskId) {
        this.taskId = taskId;
        this.startTime = System.currentTimeMillis();
        this.expireTime = this.startTime + DELAY;
        this.createTime = new Date();
    }

    public AbstractDelayedTask(String taskId, int delay) {
        this.taskId = taskId;
        this.startTime = System.currentTimeMillis();
        this.expireTime = this.startTime + delay * 1000L;
        this.createTime = new Date();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expireTime - System.currentTimeMillis(), TimeUnit.MICROSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (o.getDelay(TimeUnit.MICROSECONDS) - this.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public void run() {
        process();
    }

    /**
     * 方法定义
     * @return
     */
    protected abstract boolean process();

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
