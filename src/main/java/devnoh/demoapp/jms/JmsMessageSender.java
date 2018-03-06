package devnoh.demoapp.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsMessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value(value = "${jms.queue:default.queue}")
    private String jmsQueue;

    public void send(String message) {
        jmsTemplate.convertAndSend(jmsQueue, message);
        log.info("Message sent: {}", message);
    }
}
