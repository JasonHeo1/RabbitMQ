package com.qf.topic;

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

        //创建exchange绑定队列 topic-queue-1  topic-queue-2
        // 动物的信息 <speed> <color> <what>
        // *.red*
        // fast.#
        // #.rabbit  *.*.rabbit
        channel.exchangeDeclare("topic-exchange", BuiltinExchangeType.TOPIC);
        channel.queueBind("topic-queue-1", "topic-exchange", "*.red.*");
        channel.queueBind("topic-queue-2", "topic-exchange", "fast.#");
        channel.queueBind("topic-queue-2", "topic-exchange", "*.*.rabbit");


        //3. 创建exchange， routing-queue-error, routing-queue-info
        //param1: exchange的名称
        //param2: 指定exchange的类型 FANOUT - pubsub, DIRECT - Routing, TOPIC -Topics
        channel.basicPublish("topic-exchange", "fast.red.monkey", null, "红快猴子".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("topic-exchange", "slow.black.dog", null, "黑慢狗".getBytes(StandardCharsets.UTF_8));
        channel.basicPublish("topic-exchange", "fast.white.cat", null, "快白猫".getBytes(StandardCharsets.UTF_8));

        //4. send msg to exchange

        //Ps: exchange是不会帮你将消息持久化到本地的，Queue 才会帮你持久化消息。
        System.out.println("生产者发布消息成功！");
        //5. release resource
        channel.close();
        connection.close();
    }
}
