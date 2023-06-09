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

    @Bean("exchangeForUpdatedLink")
    public DirectExchange directExchangeForUpdatedLink() {
        return new DirectExchange(config.rabbit().exchange().botForUpdatedLinkExchange());
    }

    @Bean("queueForUpdatedLink")
    public Queue directQueueForUpdatedLink() {
        return QueueBuilder.durable(config.rabbit().queue().botForUpdatedLinkQueue())
                .withArgument("DLX_ARGUMENT_NAME", config.rabbit().exchange().botForUpdatedLinkExchange() + ".dlx")
                .build();
    }

    @Bean("bindingDirectForUpdatedLink")
    public Binding directBindingForUpdatedLink(Queue directQueue, DirectExchange directExchange) {
        return BindingBuilder
                .bind(directQueue)
                .to(directExchange)
                .with(config.rabbit().routingKey().botForUpdatedLinkRoutingKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            CachingConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        rabbitTemplate.setExchange(config.rabbit().exchange().botForUpdatedLinkExchange());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
