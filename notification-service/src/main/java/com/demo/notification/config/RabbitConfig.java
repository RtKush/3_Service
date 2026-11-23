package com.demo.notification.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("!stub")
public class RabbitConfig {
    public static final String EXCHANGE="sc360.events";
    public static final String QUEUE="notification.events";
    @Bean TopicExchange notificationExchange(){return new TopicExchange(EXCHANGE);}
    @Bean Queue notificationQueue(){return QueueBuilder.durable(QUEUE).build();}
    @Bean Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange){
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("#");
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
