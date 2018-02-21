package devnoh.demoapp.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class SomeRestClient {

    private RestTemplate restTemplate;

    @Autowired
    public SomeRestClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public String getOriginIp() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://httpbin.org/ip", String.class);

            String responseBody = response.getBody();
            log.debug("statusCode={}, responseBody={}", response.getStatusCode(), responseBody);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException(responseBody);
            }

            ApiResult apiResult = new ObjectMapper().readValue(responseBody, ApiResult.class);
            return apiResult.getOrigin();

        } catch (Throwable t) {
            log.error(t.getMessage(), t);
            throw new RestClientException("Failed to get the origin IP address.", t);
        }
    }

    @Getter
    @Setter
    static class ApiResult {
        private String origin;
    }

}
