package com.qf.pubsub;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer2 {

    @Test
    public void consume() throws IOException, TimeoutException {
        //1. 获取链接对象
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建channel
        Channel channel = connection.createChannel();

        //3. 声明队列-HelloWorld
        channel.queueDeclare("pubsub-queue2", true, false, false, null);

        //3.5 指定当前消费者，一次消费多少个信息
        channel.basicQos(1);

        //4. 开启监听Queue
        DefaultConsumer consume = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者2号接收到消息： "+ new String(body, "UTF-8"));

                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        channel.basicConsume("pubsub-queue2", false, consume);

        System.out.println("消费者开始监听队列！");
        //System.in.read();
        System.in.read();

        //5. 释放资源
        channel.close();
        connection.close();
    }
}
