package com.qf.helloworld;

import com.qf.config.RabbitMQClient;
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

        //3. send msg to exchange
        String msg = "Hello-World!";
        //param1: 指定 exchange use"".
        //param2: 指定 router rule, use 具体的队列名称
        //param3: 指定传递的消息所携带的 properties, use null.
        //param4: define release detail msg, byte[]
        channel.basicPublish("","HelloWorld",null,msg.getBytes(StandardCharsets.UTF_8));
        //Ps: exchange是不会帮你将消息持久化到本地的，Queue 才会帮你持久化消息。
        System.out.println("生产者发布消息成功！");
        //4. release resource
        channel.close();
        connection.close();
    }

}
