package de.helfenkannjeder.helfomat.infrastructure.kafka;

import de.helfenkannjeder.helfomat.core.organisation.OrganisationId;
import de.helfenkannjeder.helfomat.core.organisation.event.OrganisationEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String kafkaServerAddress;

    @Value("${kafka.topic.organisation-events}")
    private String kafkaTopic;

    @Bean
    public KafkaTemplate<OrganisationId, OrganisationEvent> kafkaTemplate() {
        KafkaTemplate<OrganisationId, OrganisationEvent> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setDefaultTopic(kafkaTopic);
        return kafkaTemplate;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<OrganisationId, OrganisationEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<OrganisationId, OrganisationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<OrganisationId, OrganisationEvent> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.KEY_DEFAULT_TYPE, OrganisationId.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, OrganisationEvent.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    private ProducerFactory<OrganisationId, OrganisationEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServerAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

}
