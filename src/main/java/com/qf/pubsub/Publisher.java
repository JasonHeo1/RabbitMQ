package com.qf.pubsub;

import com.qf.config.RabbitMQClient;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class Publisher {

    @Test
    public void publish() throws Exception {
        //1. get Connection
        Connection connection = RabbitMQClient.getConnection();

        //2. create Channel
        Channel channel = connection.createChannel();

        //3. 创建exchange 绑定某一个队列
        //param1: exchange的名称
        //param2: 指定exchange的类型 FANOUT - pubsub, DIRECT - Routing, TOPIC -Topics
        channel.exchangeDeclare("pubsub-exchange", BuiltinExchangeType.FANOUT);
        channel.queueBind("pubsub-queue1", "pubsub-exchange", "");
        channel.queueBind("pubsub-queue2", "pubsub-exchange", "");

        //4. send msg to exchange
        for (int i = 0; i < 10; i++) {
            String msg = "Hello-World!" + i;
            channel.basicPublish("pubsub-exchange", "Work", null, msg.getBytes(StandardCharsets.UTF_8));
        }

        //Ps: exchange是不会帮你将消息持久化到本地的，Queue 才会帮你持久化消息。
        System.out.println("生产者发布消息成功！");
        //5. release resource
        channel.close();
        connection.close();
    }
}
