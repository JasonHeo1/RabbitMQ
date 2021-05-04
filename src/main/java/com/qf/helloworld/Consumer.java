package com.qf.helloworld;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    @Test
    public void consume() throws IOException, TimeoutException {
        //1. 获取链接对象
        Connection connection = RabbitMQClient.getConnection();

        //2. 创建channel
        Channel channel = connection.createChannel();

        //3. 声明队列-HelloWorld
        //param1: queue 指定队列的名称
        //param2: durable 当前队列是否需要持久化(true)
        //param3: exclusive 是否排外（conn.close()  当前队列会被自动删除， 当前队列只能被一个消费者消费）
        //param4: autoDelete 如果这个队列没有消费者消费，队列自动删除
        //param5: arguments 指定当前队列其他信息
        channel.queueDeclare("HelloWorld", true, false, false, null);


        //4. 开启监听Queue
        DefaultConsumer consume = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收到消息： "+ new String(body, "UTF-8"));
            }
        };
        //param1: queue 指定消费那个队列
        //param2: autoAck 指定是否自动ACK （true, 接收到消息后，会立即告诉RabbitMQ)
        //param3: consumer 指定消费回调
        channel.basicConsume("HelloWorld", true, consume);

        System.out.println("消费者开始监听队列！");
        //System.in.read();
        System.in.read();

        //5. 释放资源
        channel.close();
        connection.close();
    }
}
