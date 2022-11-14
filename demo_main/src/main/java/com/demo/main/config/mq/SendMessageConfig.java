package com.demo.main.config.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * product -> message -> exchange -> queue -> consumer
 */
@Configuration
public class SendMessageConfig {

    @Bean
    public Queue defaultQueue() {
        return new Queue("demo");
    }

    /**
     * ---- direct exchange ----
     *
     */
    @Bean
    public Queue dirQueue1() {
        return new Queue("demo_dir_demo1");
    }

    @Bean
    public Queue dirQueue2() {
        return new Queue("demo_dir_demo2");
    }

    @Bean
    DirectExchange dirExchange() {
        return new DirectExchange("demo_dir_exchange");
    }

    @Bean
    Binding bindingDirExchange(Queue dirQueue1, DirectExchange dirExchange) {
        return BindingBuilder
                .bind(dirQueue1).to(dirExchange).with("demo_dir_rt1");
    }

    @Bean
    Binding bindingDirExchange2(Queue dirQueue2, DirectExchange dirExchange) {
        return BindingBuilder
                .bind(dirQueue2).to(dirExchange).with("demo_dir_rt2");
    }


    // fanout exchange
    @Bean
    public Queue ftQueue1() {
        return new Queue("demo_ft_demo1");
    }

    @Bean
    public Queue ftQueue2() {
        return new Queue("demo_ft_demo2");
    }

    @Bean
    FanoutExchange ftExchange() {
        return new FanoutExchange("demo_ft_exchange");
    }

    @Bean
    Binding bindingFtExchange(Queue ftQueue1, FanoutExchange ftExchange) {
        return BindingBuilder
                .bind(ftQueue1).to(ftExchange);
    }

    @Bean
    Binding bindingFtExchange2(Queue ftQueue2, FanoutExchange ftExchange) {
        return BindingBuilder
                .bind(ftQueue2).to(ftExchange);
    }

    // topic exchange
    @Bean
    public Queue tpQueue1() {
        return new Queue("demo_tp_demo1");
    }

    @Bean
    public Queue tpQueue2() {
        return new Queue("demo_tp_demo2");
    }

    @Bean
    TopicExchange tpExchange() {
        return new TopicExchange("demo_tp_exchange");
    }

    @Bean
    Binding bindingTpExchange(Queue tpQueue1, TopicExchange tpExchange) {
        return BindingBuilder
                .bind(tpQueue1).to(tpExchange).with("demo_tp_*");
    }

    @Bean
    Binding bindingTpExchange2(Queue tpQueue2, TopicExchange tpExchange) {
        return BindingBuilder
                .bind(tpQueue2).to(tpExchange).with("demo_*_*");
    }

    @Bean
    Binding bindingTpExchange22(Queue tpQueue2, TopicExchange tpExchange) {
        return BindingBuilder
                .bind(tpQueue2).to(tpExchange).with("demo.*.*");
    }

}
