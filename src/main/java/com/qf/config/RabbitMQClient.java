package com.qf.config;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQClient {

    public static Connection getConnection(){

        // Create Connection factory
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.17.236.83");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/test");

        // Create Connection
        Connection conn = null;
        try {
            conn = factory.newConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }
}
