package devnoh.demoapp.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.regex.Pattern;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(SomeRestClient.class)
@Slf4j
public class SomeRestClientTest {

    private static String ENDPOINT_URL = "http://httpbin.org/ip";

    @Autowired
    private SomeRestClient client;

    @Autowired
    private MockRestServiceServer server;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getOriginIp() {
        this.server
                .expect(requestTo(ENDPOINT_URL))
                .andRespond(withSuccess("{\"origin\": \"98.174.154.130\"}", MediaType.APPLICATION_JSON));

        String response = client.getOriginIp();
        log.info("response={}", response);

        assertNotNull(response);
        assertEquals("98.174.154.130", response);
    }

    @Test
    public void getOriginIpWithServerError() throws Exception {
        exception.expect(RestClientException.class);
        exception.expectMessage(Pattern.compile("Failed to get").pattern());

        this.server
                .expect(requestTo(ENDPOINT_URL))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        String response = client.getOriginIp();
        log.info("response={}", response);
    }
}
