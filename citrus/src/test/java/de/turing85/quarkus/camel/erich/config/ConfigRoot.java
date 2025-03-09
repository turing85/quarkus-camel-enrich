package de.turing85.quarkus.camel.erich.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ Http.class, Jms.class })
public class ConfigRoot {
}
