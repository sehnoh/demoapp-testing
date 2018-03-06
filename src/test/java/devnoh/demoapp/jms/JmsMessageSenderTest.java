package devnoh.demoapp.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = {"classpath:application-test.properties"})
@Slf4j
public class JmsMessageSenderTest {

    private static String JMS_QUEUE_NAME = "default.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private JmsMessageSender jmsMessageSender;

    private BrokerService broker;

    @Before
    public void setUp() throws Exception {
        /*
        broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseShutdownHook(false);
        broker.start();
        */
    }

    @After
    public void tearDown() throws Exception {
        /*
        broker.stop();
        */
    }

    @Test(timeout = 5000)
    public void send() throws Exception {
        jmsMessageSender.send("A test message");

        // Gives enough time to wait for fetching a message from the queue.
        Thread.sleep(2000);

        Object object = jmsTemplate.receiveAndConvert(JMS_QUEUE_NAME);
        log.debug("object={}", object);

        assertNotNull(object);
        assertTrue(object instanceof String);
        assertEquals("A test message", object.toString());
    }
}
