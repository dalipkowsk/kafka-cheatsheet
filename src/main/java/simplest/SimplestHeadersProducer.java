package simplest;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SimplestHeadersProducer {

    public static void main(String[] args) {
        System.out.println("Simplest Headers Producer");

        final Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "simplest-producer");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final ProducerRecord<String, String> message = new ProducerRecord<>("simplest-headers-topic", "Hello simplest topic! Time: " + Instant.now());
        message.headers().add("type", "plain-text".getBytes());
        message.headers().add("author", "John Smith".getBytes());

        try (KafkaProducer<String, String> simplestProducer = new KafkaProducer<>(properties)) {
            RecordMetadata recordMetadata = simplestProducer.send(message).get();
            System.out.println("Message sent successfully! timestamp: " + recordMetadata.timestamp() + ", offset: " + recordMetadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error during message sending: " + e.getMessage());
        }
    }
}
