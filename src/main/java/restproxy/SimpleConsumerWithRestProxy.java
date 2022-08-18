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
public class SimpleConsumerWithRestProxy {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        final HttpClient httpClient = HttpClient.newHttpClient();

        //1. Create consumer instance
        HttpRequest httpRequestCreateConsumerInstance = HttpRequest
                .newBuilder(URI.create("http://localhost:8082/consumers/my-string-consumer"))
                .header("Content-Type", "application/vnd.kafka.json.v2+json")
                .header("Accept", "application/vnd.kafka.json.v2+json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"my_consumer_instance\", \"format\": \"json\", \"auto.offset.reset\": \"earliest\"}"))
                .build();

        //2. Send subscription
        HttpRequest httpRequestSubscription = HttpRequest
                .newBuilder(URI.create("http://localhost:8082/consumers/my-string-consumer/instances/my_consumer_instance/subscription"))
                .header("Content-Type", "application/vnd.kafka.json.v2+json")
                .header("Accept", "application/vnd.kafka.json.v2+json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"topics\": [\"simplest-topic\"]}"))
                .build();

        //3. Consume messages
        HttpRequest httpRequestConsume = HttpRequest
                .newBuilder(URI.create("http://localhost:8082/consumers/my-string-consumer/instances/my_consumer_instance/records"))
                .header("Content-Type", "application/vnd.kafka.json.v2+json")
                .header("Accept", "application/vnd.kafka.json.v2+json")
                .GET()
                .build();

        HttpResponse.BodyHandler<String> httpResponseHandler = HttpResponse.BodyHandlers.ofString();
        try {
            //These two lines should be executed once
            HttpResponse<String> httpResponseConsumerInstance = httpClient.send(httpRequestCreateConsumerInstance, httpResponseHandler);
            System.out.println("1. Create consumer instance: " + httpResponseConsumerInstance.body());

            HttpResponse<String> httpResponseSubscription = httpClient.send(httpRequestSubscription, httpResponseHandler);
            System.out.println("2. Request subscription: " + httpResponseSubscription.body());

            HttpResponse<String> httpResponseConsume = httpClient.send(httpRequestConsume, httpResponseHandler);
            System.out.println("3. Consume messages: " + httpResponseConsume.body());
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to send HTTP request to HTTP Kafka Proxy");
            Thread.currentThread().interrupt();
        }
    }
}
