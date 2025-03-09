package de.turing85.quarkus.camel.erich.config;

import java.time.Duration;

import org.citrusframework.http.server.HttpServer;
import org.citrusframework.http.server.HttpServerBuilder;
import org.springframework.context.annotation.Bean;

public class Http {
  public static final String SERVER_NAME = " httpServer";

  @Bean(SERVER_NAME)
  public HttpServer httpServer() {
    // @formatter:off
    return new HttpServerBuilder()
        .port(9999)
        .timeout(Duration.ofSeconds(1).toMillis())
        .autoStart(true)
        .build();
    // @formatter:on
  }
}
