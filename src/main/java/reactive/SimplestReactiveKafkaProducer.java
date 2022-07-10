package reactive;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.time.Duration;
import java.util.Properties;

/**
 * https://projectreactor.io/docs/kafka/release/reference/
 */
public class SimplestReactiveKafkaProducer {

    public static void main(String[] args) {
        System.out.println("Simplest Reactive Kafka Producer");

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final SenderOptions<String, String> senderOptions =
                SenderOptions.<String, String>create(properties)
                        .maxInFlight(1024);

        final KafkaSender<String, String> sender = KafkaSender.create(senderOptions);
        final Flux<SenderRecord<String, String, Integer>> messages =
                Flux.range(1, 100)
                        .delayElements(Duration.ofSeconds(1))
                        .map(i -> {
                            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("simplest-reactive-topic", "Message no. " + i);
                            return SenderRecord.create(producerRecord, i);
                        });

        sender.send(messages)
                .doOnError(e-> System.out.println("Send failed: " + e))
                .doOnNext(r -> System.out.printf("Message #%d send response: %s\n", r.correlationMetadata(), r.recordMetadata()))
                .subscribe();
    }
}
