package de.turing85.quarkus.camel.erich.config;

import jakarta.jms.ConnectionFactory;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.citrusframework.dsl.endpoint.CitrusEndpoints;
import org.citrusframework.jms.endpoint.JmsEndpoint;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

public class Jms {
  public static final String ENDPOINT = "jmsEndpoint";
  public static final String CONNECTION_FACTORY = "connectionFactory";

  @Bean
  @Qualifier(CONNECTION_FACTORY)
  public ConnectionFactory connectionFactory() {
    return new ActiveMQConnectionFactory("tcp://localhost:61616");
  }

  @Bean
  @Qualifier(ENDPOINT)
  public JmsEndpoint jmsEndpoint(
      @Qualifier(CONNECTION_FACTORY) ConnectionFactory connectionFactory) {
    // @formatter:off
    return CitrusEndpoints.jms()
        .asynchronous()
        .connectionFactory(connectionFactory)
        .destination("input")
        .pubSubDomain(true)
        .build();
    // @formatter:on
  }
}
