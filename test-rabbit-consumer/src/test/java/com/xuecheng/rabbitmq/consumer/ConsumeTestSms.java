package com.xuecheng.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 星仔
 * @Date: 2018/12/24 22:43
 * @Description:
 */
public class ConsumeTestSms {

    //声明队列名称
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //声明交换机的名称
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //声明RoutingKey
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂，建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        //创建虚拟主机,rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
        factory.setVirtualHost("/");
        //创建于MQ服务的TCP连接
        Connection connection = factory.newConnection();
        //创建于EXchange的通道，每一个通道都相当于一个会话事务
        Channel channel = connection.createChannel();
        /**
         * 参数明细：
         * exchange:交换机名称
         * type：交换机的类型
         *      fanout,topic,direct,headers
         */
        channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
        channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);
        //声明队列
        channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
        //声明消费消息的方法
        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * 消费者接收消息调用此方法
             * @param consumerTag 消费者的标签，在channel.basicConsume()去指定
             * @param envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * @param properties 消息属性
             * @param body 消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException{
                //交换机
                String exchange = envelope.getExchange();
                //路由key
                String routingKey = envelope.getRoutingKey();
                //消息id,mq在channel中用来标识消息的id,用于确认消息已接收
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String msg = new String(body,"utf-8"); System.out.println("receive message.." + msg);
            }
        };
        /**
         * 监听队列
         * 参数：String queue, boolean autoAck,Consumer callback
         * 参数明细：
         * 1.queue： 队列名称
         * 2.autoAck： 自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置为false则需要手动回复
         * 3.callback： 消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE_INFORM_SMS,true,consumer);
    }
}