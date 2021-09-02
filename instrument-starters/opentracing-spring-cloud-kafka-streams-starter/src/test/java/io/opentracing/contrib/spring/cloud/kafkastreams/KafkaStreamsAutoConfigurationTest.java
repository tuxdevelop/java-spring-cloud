package io.opentracing.contrib.spring.cloud.kafkastreams;

import static org.mockito.Mockito.mock;

import io.opentracing.Tracer;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer;


public class KafkaStreamsAutoConfigurationTest {

  @Test
  public void testStreamsBuilderFactoryBeanCustomizerIsInitialized() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(TestStreamConfiguration.class, KafkaStreamsAutoConfiguration.class,
        KafkaStreamsAutoConfiguration.class);
    context.refresh();
    StreamsBuilderFactoryBeanCustomizer customizer = context.getBean(
        StreamsBuilderFactoryBeanCustomizer.class);
    BDDAssertions.then(customizer).isNotNull();
  }

  @Test(expected = NoSuchBeanDefinitionException.class)
  public void testStreamsBuilderFactoryBeanCustomizerIsNotInitializedWhenDisabled() {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.register(TestStreamConfiguration.class, KafkaStreamsAutoConfiguration.class,
        KafkaStreamsAutoConfiguration.class);
    TestPropertyValues.of("opentracing.spring.cloud.kafka-streams.enabled:false").applyTo(context);
    context.refresh();
    context.getBean(StreamsBuilderFactoryBeanCustomizer.class);
  }

  @Configuration
  static class TestStreamConfiguration {

    @Bean
    public Tracer tracer() {
      return mock(Tracer.class);
    }

  }
}
