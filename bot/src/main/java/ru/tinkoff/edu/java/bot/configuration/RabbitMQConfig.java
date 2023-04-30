package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final ApplicationConfig config;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(config.rabbit().host());
        cachingConnectionFactory.setUsername(config.rabbit().username());
        cachingConnectionFactory.setPassword(config.rabbit().password());
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean("exchangeUpdate")
    public DirectExchange directExchange() {
        return new DirectExchange(config.rabbit().exchange().updateExchange());
    }

    @Bean("queueUpdate")
    public Queue directQueue1() {
        return new Queue(config.rabbit().queue().UpdateQueue());
    }

    @Bean("bindingDirect")
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with(config.rabbit().routingKey().updateRoutingKey());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
