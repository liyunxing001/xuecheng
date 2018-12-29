package com.xuecheng.rabbitmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Auther: 星仔
 * @Date: 2018/12/24 21:31
 * @Description:
 */
public class ProducerTest04 {


    //声明队列名称
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    //声明交换机的名称
    private static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    //声明RoutingKey
    private static final String ROUTINGKEY_SMS = "inform.#.sms.#";
    private static final String ROUTINGKEY_EMAIL = "inform.#.email.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = null;
        Channel channel = null;

        try {
            //创建连接工厂，建立连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            //创建虚拟主机,rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost("/");
            //创建于MQ服务的TCP连接
            connection = factory.newConnection();
            //创建于EXchange的通道，每一个通道都相当于一个会话事务
            channel = connection.createChannel();

            //声明交换机 String exchange,BuiltinExchangeType type
            /**
             * 参数明细：
             * exchange:交换机名称
             * type：交换机的类型
             *      fanout,topic,direct,headers
             */
            channel.exchangeDeclare(EXCHANGE_TOPICS_INFORM, BuiltinExchangeType.TOPIC);
            //声明队列，如果RabbitMQ中没有该队列，则会创建
            /**
             *参数：String queue，boolean durable，boolean exclusive，boolean autoDelete，Map<String,Object> params
             *参数明细：
             * 1. queue：队列名称
             * 2. durable：是否持久化，如果持久化，将MQ重启之后队列还在
             * 3. exclusive： 是否独占连接，队列只允许在该队列中访问，一旦连接关闭，该队列将自动删除，如果将此参数设置为true，那么可用于临时队列的创建
             * 4. autoDelete：自动删除，队列不再使用时是否关闭，如果将此参数设置为true将exclusive设置为true，可用于创建临时队列
             * 5. params: 可以设置队列的一些扩展参数，比如设置存活时间等等
             */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            //交换机和队列进行绑定String queue, String exchange, String routingKey
            /**
             * 参数明细：
             * queue:队列的名称
             * exchange:交换机的名称
             * routinfkey:路由key
             */
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_SMS);
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_TOPICS_INFORM,ROUTINGKEY_EMAIL);
            //发送消息
            /**
             * 参数：String exchange,String routingKey,String props,Byte[] body
             * 参数明细：
             * 1、exchange： 交换机，如果不使用，将使用MQ的默认交换机
             * 2、routingKey： 路由key，交换机根据路由key将消息转发到指定的队列，如果使用默认交换机，routingKey设置为队列的名称
             * 3、props：消息的属性
             * 4、body： 消息内容
             */
//            for (int i=0; i<5;i++){
//                String msg = "send message to sms"+i;
//                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms",null,msg.getBytes());
//            }
//            for (int i=0; i<5;i++){
//                String msg = "send message to email"+i;
//                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.email",null,msg.getBytes());
//            }
            for (int i=0; i<5;i++){
                String msg = "send message to sms and email"+i;
                channel.basicPublish(EXCHANGE_TOPICS_INFORM,"inform.sms.email",null,msg.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            //关闭连接，先关闭通道，在关闭连接
            if(channel!=null){
                channel.close();
            }
            if(connection!=null){
                connection.close();
            }
        }
    }

}