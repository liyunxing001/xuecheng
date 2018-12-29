package com.xuecheng.rabbitmq.producer;


import com.xuecheng.test.config.RabbitMQConfig;
import com.xuecheng.test.config.RabbitmqApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Auther: 星仔
 * @Date: 2018/12/24 21:31
 * @Description:
 */
@SpringBootTest(classes = RabbitmqApplication.class)
@RunWith(SpringRunner.class)
public class ProducerTest05 {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void rabbitMQTest(){
        String message = "send message";
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_TOPICS_INFORM,"inform.sms.email",message);
    }
}