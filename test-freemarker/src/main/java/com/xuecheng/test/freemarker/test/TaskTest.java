package com.xuecheng.test.freemarker.test;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Auther: 星仔
 * @Date: 2018/12/24 20:27
 * @Description:
 */
@Component
public class TaskTest {
    @Scheduled(cron = "0/5 * * * * ? ")
    //需要注意@Scheduled这个注解，它可配置多个属性：cron\fixedDelay\fixedRate
    public static void test() {
        System.out.println("小说城 www.xiaoshuocity.com 每分钟执行一次:" );
    }
}