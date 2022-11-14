package com.demo.main.config.mq.util;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainTest {

    private static String QUEUE_NAME = "test_queue";

    public static void main(String[] args) throws Exception {

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println("执行");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("over");
        });

        try {
            future.get(3000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("超时");
            e.printStackTrace();
        }


//        send();
//
//        Thread.sleep(5000);
//        consumer();
    }

    private static void send() throws Exception {

        // 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 建立信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        for (int i = 0; i < 10; i++) {
            // 发送消息
            String message = "message + " + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        // 关闭资源
        channel.close();
        connection.close();

        System.out.println("over 1");
    }


    private static void consumer() throws Exception {
        // 建立连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 建立信道
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 消息接收回调方法
        DeliverCallback deliverCallback = (tag, delivery) -> {
            String message = new String(delivery.getBody());
            System.out.println("receive message: " + message);
        };

        // 消息取消回调方法
        CancelCallback cancelCallback = (tag) -> {
            System.out.println(tag);
        };

        // 监听消息
        String s = channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

        System.out.println("over 2 " + s);
    }
}
