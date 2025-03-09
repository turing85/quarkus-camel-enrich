package de.turing.camel.quarkus.enrich;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.ConnectionFactory;

import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import static org.apache.camel.builder.endpoint.StaticEndpointBuilders.jms;

@ApplicationScoped
@RequiredArgsConstructor
public class MyRoute extends RouteBuilder {
  private final ConnectionFactory connectionFactory;

  @Override
  public void configure() {
    // @formatter:off
    from(
        jms("topic:input::input")
            .connectionFactory(connectionFactory))
        .log("Received: ${body}")
        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/xml"))
        .enrich("http://localhost:9999/enrich")
        .log("after enhance: ${body}");
    // @formatter:on
  }
}
