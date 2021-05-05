package com.qf.routing;

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

        //3. 创建exchange， routing-queue-error, routing-queue-info
        //param1: exchange的名称
        //param2: 指定exchange的类型 FANOUT - pubsub, DIRECT - Routing, TOPIC -Topics
        channel.exchangeDeclare("routing-exchange", BuiltinExchangeType.DIRECT);
        channel.queueBind("routing-queue-error", "routing-exchange", "ERROR");
        channel.queueBind("routing-queue-info", "routing-exchange", "INFO");

        //4. send msg to exchange
        channel.basicPublish("routing-exchange", "ERROR",null,"ERROR".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("routing-exchange", "INFO",null,"INFO1".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("routing-exchange", "INFO",null,"INFO2".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("routing-exchange", "INFO",null,"INFO3".getBytes(StandardCharsets.UTF_8));

        //Ps: exchange是不会帮你将消息持久化到本地的，Queue 才会帮你持久化消息。
        System.out.println("生产者发布消息成功！");
        //5. release resource
        channel.close();
        connection.close();
    }
}
