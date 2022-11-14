package com.demo.main.demo.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/rabbit")
public class ProductController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * default exchange
     */
    @RequestMapping("/product")
    public void sendMessage() {
        rabbitTemplate.convertAndSend("demo", "hello rabbit");
    }


    /**
     * dir exchange
     */
    @RequestMapping("/product/dir")
    public void sendMessageDirExchange(String key) {
        rabbitTemplate.convertAndSend("demo_dir_exchange", key, "hello rabbit im dir exchange key=" + key);
    }

    /**
     * fanout exchange
     */
    @RequestMapping("/product/ft")
    public void sendMessageFtExchange() {
        rabbitTemplate.convertAndSend("demo_ft_exchange", null, "hello rabbit im ft exchange");
    }

    /**
     * topic exchange
     */
    @RequestMapping("/product/tp")
    public void sendMessageTpExchange(String key) {
        rabbitTemplate.convertAndSend("demo_tp_exchange", key, "hello rabbit im ft exchange key=" + key);
    }

}
