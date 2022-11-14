package com.demo.main.demo.rabbit;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DemoConsumer {

    @RabbitListener(queues = "demo")
    public void receiveMessage(String message) {
        System.out.println(message);
    }

    @RabbitListener(queues = "demo_dir_demo1")
    public void receiveMessageDir1(String message) {
        System.out.println("1:" + message);
    }

    @RabbitListener(queues = "demo_dir_demo2")
    public void receiveMessageDir2(String message) {
        System.out.println("2:" + message);
    }

    @RabbitListener(queues = "demo_ft_demo1")
    public void receiveMessageFt1(String message) {
        System.out.println("ft1:" + message);
    }

    @RabbitListener(queues = "demo_ft_demo2")
    public void receiveMessageFt2(String message) {
        System.out.println("ft2:" + message);
    }

    @RabbitListener(queues = "demo_ft_demo2")
    public void receiveMessageFt22(String message) {
        System.out.println("ft22:" + message);
    }

    @RabbitListener(queues = "demo_tp_demo1")
    public void receiveMessageTp1(String message) {
        System.out.println("tp1:" + message);
    }

    @RabbitListener(queues = "demo_tp_demo2")
    public void receiveMessageTp2(String message) {
        System.out.println("tp2:" + message);
    }
}
