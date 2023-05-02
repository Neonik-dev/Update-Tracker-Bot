package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.api.LinkUpdateRequest;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private final ApplicationConfig config;
    private static final String DTO_PATH_FOR_RABBIT = "ru.tinkoff.edu.java.scrapper.clients.dto.LinkUpdateRequest";

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

    @Bean
    public ClassMapper classMapper(){
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(
                Map.of(DTO_PATH_FOR_RABBIT, LinkUpdateRequest.class)
        );
        return classMapper;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(MessageConverter jsonMessageConverter, CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }

    @Bean("dlxUpdateLink")
    public FanoutExchange dlxUpdateLink() {
        return ExchangeBuilder.fanoutExchange(config.rabbit().exchange().updateDLX() + ".dlx").build();
    }

    @Bean("dlqUpdateLink")
    public Queue dlqUpdateLink() {
        return QueueBuilder.durable(config.rabbit().queue().updateDLQ() + ".dlq").build();
    }

    @Bean("dlqBinding")
    public Binding dlqBinding(FanoutExchange exchange, Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(classMapper);
        return messageConverter;
    }
}
