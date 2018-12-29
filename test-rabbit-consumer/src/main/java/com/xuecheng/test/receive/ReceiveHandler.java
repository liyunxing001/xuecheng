package com.xuecheng.test.receive;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Auther: 星仔
 * @Date: 2018/12/29 11:26
 * @Description:
 */
@Component
public class ReceiveHandler {
    //监听队列
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_SMS})
    public void receive_sms(String msg, Message message, Channel channel){
        System.out.println(msg);
        System.out.println(message.getBody());
    }

    //监听队列
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(String msg, Message message, Channel channel){
        System.out.println(msg);
        System.out.println(message.getBody());
    }
}