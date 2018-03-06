package devnoh.demoapp.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JmsMessageListener {

    @JmsListener(destination = "${jms.queue:default.queue}")
    public void receive(String message) {
        log.info("Message received: {}", message);
    }
}
