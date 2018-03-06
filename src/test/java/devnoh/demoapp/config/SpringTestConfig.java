package devnoh.demoapp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jms.annotation.EnableJms;

@TestConfiguration
@EnableJms
public class SpringTestConfig {

    /*
    private static final String JMS_BROKER_URL = "vm://localhost?broker.persistent=false";

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(JMS_BROKER_URL);
    }
    */

}
