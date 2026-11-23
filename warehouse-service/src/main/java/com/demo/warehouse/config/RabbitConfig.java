package com.demo.warehouse.config;
import org.springframework.amqp.core.*; import org.springframework.context.annotation.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("!stub")
public class RabbitConfig {
    public static final String EXCHANGE="sc360.events";
    public static final String QUEUE="warehouse.stock.events";
    public static final String ROUTING_KEY="stock.event";
    @Bean TopicExchange warehouseExchange(){return new TopicExchange(EXCHANGE);}
    @Bean Queue stockQueue(){return QueueBuilder.durable(QUEUE).build();}
    @Bean Binding stockBinding(Queue stockQueue, TopicExchange warehouseExchange){
        return BindingBuilder.bind(stockQueue).to(warehouseExchange).with(ROUTING_KEY);
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
