package de.turing85.quarkus.camel.erich;

import static org.citrusframework.actions.SendMessageAction.Builder.send;
import static org.citrusframework.container.Async.Builder.async;
import static org.citrusframework.http.actions.HttpActionBuilder.http;

import de.turing85.quarkus.camel.erich.config.ConfigRoot;
import de.turing85.quarkus.camel.erich.config.Http;
import de.turing85.quarkus.camel.erich.config.Jms;
import org.citrusframework.TestCaseRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.http.server.HttpServer;
import org.citrusframework.jms.endpoint.JmsEndpoint;
import org.citrusframework.message.MessageType;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@ContextConfiguration(classes = ConfigRoot.class)
public class CitrusTestIT extends TestNGCitrusSpringSupport {
  @Autowired
  @Qualifier(Http.SERVER_NAME)
  HttpServer httpServer;

  @Autowired
  @Qualifier(Jms.ENDPOINT)
  JmsEndpoint jmsEndpoint;

  @Test
  @CitrusTest
  public void test(@Optional @CitrusResource TestCaseRunner runner) {
    // when
    runner.when(send(jmsEndpoint)
        .message()
        .type(MessageType.XML)
        .body("""
            <?xml version="1.0" encoding="UTF-8"?>
            <foo>föö</foo>"""));

    // then
    runner.then(async().actions(
        http().server(httpServer)
            .receive()
            .post("/enrich")
            .message()
            .contentType("application/xml")
            .type(MessageType.XML)
            .body("""
                <?xml version="1.0" encoding="UTF-8"?>
                <foo>föö</foo>"""),
        http().server(httpServer)
            .send()
            .response(HttpStatus.OK)
            .message()
            .contentType("application/xml")
            .body("""
                <?xml version="1.0" encoding="UTF-8"?>
                <bar>bär</bar>""")));
  }
}
