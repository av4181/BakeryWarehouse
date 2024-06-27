package org.example.warehouse2.config;

import org.example.warehouse2.messaging.InventoryUpdateMessageListener;
import org.example.warehouse2.messaging.ProductIngredientsOrderListener;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // US-55 trigger voor het bijbestellen van een ingrediënt.  Herbestellen onder de 10 en steeds 50 stuks bijbestellen
    public static final int REORDER_THRESHOLD = 10;
    public static final int REORDER_AMOUNT = 50;
    public static final String WAREHOUSE_EXCHANGE = "warehouse-exchange";

    // 2 queue's: update stock naar supplier en delivery confirmation als antwoord op Bakery
    public static final String INVENTORY_UPDATE_QUEUE = "inventory-update-queue";
    public static final String PRODUCT_INGREDIENTS_ORDER_QUEUE = "product-ingredients-order-queue";

    // routing keys 2 queue's
    public static final String INVENTORY_UPDATE_ROUTING_KEY = "inventory.updated";
    public static final String PRODUCT_INGREDIENTS_ORDER_ROUTING_KEY = "bakery.order";


    // TopicExchange klasse van Spring AMQP / RabbitMQ
    @Bean
    public TopicExchange warehouseExchange() {
        return new TopicExchange(WAREHOUSE_EXCHANGE);
    }

    // niet persistent dus queue is niet durable
    @Bean
    public Queue inventoryUpdateQueue() {
        return new Queue(INVENTORY_UPDATE_QUEUE, false);
    }

    @Bean
    public Queue productIngredientsOrderQueue() {
        return new Queue(PRODUCT_INGREDIENTS_ORDER_QUEUE, false);
    }
    // Routing keys verbinden de queue's met de exchange
    @Bean
    public Binding inventoryUpdateBinding(TopicExchange warehouseExchange, Queue inventoryUpdateQueue) {
        return BindingBuilder.bind(inventoryUpdateQueue)
                .to(warehouseExchange)
                .with(INVENTORY_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding productIngredientsOrderBinding(TopicExchange warehouseExchange, Queue productIngredientsOrderQueue) {
        return BindingBuilder.bind(productIngredientsOrderQueue)
                .to(warehouseExchange)
                .with(PRODUCT_INGREDIENTS_ORDER_ROUTING_KEY);
    }

    // Message Converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("org.example.warehouse2.model");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    //RabbitMQ template
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    //Listener container
    @Bean
    public SimpleMessageListenerContainer inventoryUpdateListenerContainer(ConnectionFactory connectionFactory,
                                                                           InventoryUpdateMessageListener inventoryUpdateListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(INVENTORY_UPDATE_QUEUE);
        container.setMessageListener(new MessageListenerAdapter(inventoryUpdateListener, "handleInventoryUpdate"));
        return container;
    }

    @Bean
    public SimpleMessageListenerContainer productIngredientsOrderListenerContainer(ConnectionFactory connectionFactory,
                                                                                   ProductIngredientsOrderListener productIngredientsOrderListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(PRODUCT_INGREDIENTS_ORDER_QUEUE);
        container.setMessageListener(new MessageListenerAdapter(productIngredientsOrderListener, "handleProductIngredientsOrder"));
        return container;
    }
}
