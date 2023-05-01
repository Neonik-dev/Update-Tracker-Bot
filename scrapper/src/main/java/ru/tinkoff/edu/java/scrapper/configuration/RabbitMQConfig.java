package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
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
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(
                config.rabbit().host(),
                config.rabbit().port()
        );
        cachingConnectionFactory.setUsername(config.rabbit().username());
        cachingConnectionFactory.setPassword(config.rabbit().password());
        return cachingConnectionFactory;
    }

    @Bean("exchangeUpdate")
    public DirectExchange directExchange() {
        return new DirectExchange(config.rabbit().exchange().botUpdateExchange());
    }

    @Bean("queueUpdate")
    public Queue directQueue1() {
        return QueueBuilder.durable(config.rabbit().queue().botUpdateQueue())
                .withArgument("x-dead-letter-exchange", config.rabbit().exchange().botUpdateExchange() + ".dlx")
                .build();
    }

    @Bean("bindingDirect")
    public Binding directBinding(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder
                .bind(directQueue1)
                .to(directExchange)
                .with(config.rabbit().routingKey().botUpdateRoutingKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            CachingConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(config.rabbit().exchange().botUpdateExchange());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
