package io.opentracing.contrib.spring.cloud.kafkastreams;

import io.opentracing.Tracer;
import io.opentracing.contrib.kafka.streams.TracingKafkaClientSupplier;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;
import org.apache.kafka.streams.KafkaClientSupplier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;

@Configuration
@ConditionalOnClass(TracingKafkaClientSupplier.class)
@ConditionalOnBean(Tracer.class)
@AutoConfigureAfter(TracerAutoConfiguration.class)
@ConditionalOnProperty(name = "opentracing.spring.cloud.kafka-streams.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaStreamsAutoConfiguration {

  @Bean
  public StreamsBuilderFactoryBeanCustomizer streamsBuilderFactoryBeanCustomizer(Tracer tracer) {
    return factoryBean -> {
      KafkaClientSupplier supplier = new TracingKafkaClientSupplier(tracer);
      factoryBean.setClientSupplier(supplier);
    };
  }

}
