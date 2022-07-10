package reactive;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Properties;

public class SimplestReactiveKafkaConsumer {

    public static void main(String[] args) {
        System.out.println("Simplest Reactive Kafka Consumer");

        final Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "simplest-reactive-consumer-group");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

        final ReceiverOptions<Integer, String> receiverOptions =
                ReceiverOptions.<Integer, String>create(properties)
                        .subscription(Collections.singleton("simplest-reactive-topic"));

        KafkaReceiver.create(receiverOptions).receiveAutoAck()
                .flatMap(batch -> batch.doOnNext(message -> {
                    //Do something with messages, write to database, process etc..
                    System.out.println(message);
                }))
                .repeat()
                .retry()
                .subscribe();
    }
}
