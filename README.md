# delay-utils
java延迟任务调度工具，可以与spring集成使用。

# spring中bean的配置：
```
    <bean id="delayScheduleService" class="com.lehoon.delay.DelayScheduleService" init-method="init" destroy-method="shutdown" scope="singleton">
        <property name="threadPoolExecutor">
            <bean class="com.lehoon.delay.FixThreadPoolExecutor" destroy-method="shutdown">
                <constructor-arg value="${delay.threadPool.corePoolSize}" />
                <constructor-arg value="${delay.threadPool.pool.maximumPoolSize}" />
                <constructor-arg value="${delay.threadPool.pool.keepAliveSecond}" />
            </bean>
        </property>
    </bean>
```
# 任务调度线程池配置参数：
```
//线程池大小
delay.threadPool.corePoolSize=5
//线程池最大数量
delay.threadPool.pool.maximumPoolSize=10
delay.threadPool.pool.keepAliveSecond=300
```
# 需要增加延迟调度任务代码：
## 实现自定义的延迟任务
1、继承AbstractDelayedTask，实现process函数。
```
public class DelayTask extends AbstractDelayedTask {

    //默认10秒后执行任务
    public DelayTask () {
        super("delay task 0001.", 10);
    }

    public DelayTask (String taskId, int delay) {
        super(taskId, delay);
    }

    @Override
    public boolean process() {
        System.out.println("我是延迟任务，我要执行了。");
        return true;
    }
}
```

2、增加延迟任务到延迟任务队列
```
   @Autowired
   DelayScheduleService delayScheduleService;
   delayScheduleService.put(new DelayTask());
```

