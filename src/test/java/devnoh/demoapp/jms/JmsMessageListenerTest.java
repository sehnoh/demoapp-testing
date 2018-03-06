package devnoh.demoapp.jms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JmsMessageListenerTest {

    private static String JMS_QUEUE_NAME = "default.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Test(timeout = 5000)
    public void receive() throws Exception {
        jmsTemplate.convertAndSend(JMS_QUEUE_NAME, "A test message");

        // Gives enough time to wait for fetching a message from the queue.
        Thread.sleep(1000);
    }
}
