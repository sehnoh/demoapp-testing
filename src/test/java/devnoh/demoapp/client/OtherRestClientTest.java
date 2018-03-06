package devnoh.demoapp.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
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

/**
 * https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/autoconfigure/web/client/RestClientTest.html
 */
@RunWith(SpringRunner.class)
@RestClientTest(OtherRestClient.class)
@AutoConfigureWebClient(registerRestTemplate = true)
@Slf4j
public class OtherRestClientTest {

    private static String ENDPOINT_URL = "http://httpbin.org/uuid";

    @Autowired
    private OtherRestClient client;

    @Autowired
    private MockRestServiceServer server;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void getUuid_OK() {
        this.server
                .expect(requestTo(ENDPOINT_URL))
                .andRespond(withSuccess("{\"uuid\": \"3f759ca4-2a5e-4721-8d08-a3f959699dd0\"}",
                        MediaType.APPLICATION_JSON));

        String response = client.getUuid();
        log.info("response={}", response);

        assertNotNull(response);
        assertEquals("3f759ca4-2a5e-4721-8d08-a3f959699dd0", response);
    }

    @Test
    public void getUuid_ServerError() throws Exception {
        exception.expect(RestClientException.class);
        exception.expectMessage(Pattern.compile("Failed to get").pattern());

        this.server
                .expect(requestTo(ENDPOINT_URL))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        String response = client.getUuid();
        log.info("response={}", response);
    }

}
