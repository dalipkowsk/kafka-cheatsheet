package restproxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Simple Http Client
 * More info https://docs.confluent.io/platform/current/tutorials/examples/clients/docs/rest-proxy.html
 */
public class SimpleProducerWithRestProxy {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        //Prepare request to HTTP proxy
        RestProxyProducerRecord record1 = new RestProxyProducerRecord("key", "message1");
        RestProxyProducerRecord record2 = new RestProxyProducerRecord("key", "message2");
        RestProxyProducerRecord record3 = new RestProxyProducerRecord("key", "message3");
        List<RestProxyProducerRecord> messages = List.of(record1, record2, record3);
        RestProxyProducerRequestBody restProxyProducerRequestBody = new RestProxyProducerRequestBody(messages);

        final String requestBody = OBJECT_MAPPER.writeValueAsString(restProxyProducerRequestBody);
        System.out.println("Tested message: " + requestBody);

        //Send request
        final HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest
                .newBuilder(URI.create("http://localhost:8082/topics/simplest-topic"))
                .header("Content-Type", "application/vnd.kafka.json.v2+json")
                .header("Accept", "application/vnd.kafka.v2+json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse.BodyHandler<String> httpResponseHandler = HttpResponse.BodyHandlers.ofString();
        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, httpResponseHandler);
            System.out.println("Kafka HTTP proxy response: " + httpResponse.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to send HTTP request to HTTP Kafka Proxy");
            Thread.currentThread().interrupt();
        }
    }
}
