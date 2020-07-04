package com.sap.slh.tax.calculation.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableAutoConfiguration
@Configuration
public class MessageBrokerConfiguration {
	
	@Value("${rabbitmq.calc.queue}")
	private String queueName;

	@Value("${rabbitmq.calc.exchange}")
	private String exchangeName;

	@Value("${rabbitmq.calc.routingKey}")
	private String routingKey;
	
	@Autowired
	private AmqpAdmin amqpAdmin;

	@PostConstruct
	public void setupQueue() {
		TopicExchange taxServiceTopicExchange = (TopicExchange) ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
		amqpAdmin.declareExchange(taxServiceTopicExchange);
		
		Queue taxCalculationQueue = QueueBuilder.durable(queueName).build();
		amqpAdmin.declareQueue(taxCalculationQueue);	
		
		Binding binding = BindingBuilder.bind(taxCalculationQueue).to(taxServiceTopicExchange).with(routingKey);
		amqpAdmin.declareBinding(binding);

	}
    
}
