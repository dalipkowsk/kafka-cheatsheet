package simplest;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Instant;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SimplestProducer {
    public static void main(String[] args) {
        System.out.println("Simplest Producer");

        final Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "simplest-producer");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final ProducerRecord<String, String> message = new ProducerRecord<>("simplest-topic", "Hello simplest topic! Time: " + Instant.now());

        final KafkaProducer<String, String> simplestProducer = new KafkaProducer<>(properties);

//FIRE AND FORGET
        Future<RecordMetadata> send = simplestProducer.send(message);

//ASYNCHRONOUS
//        simplestProducer.send(message, (Callback) (recordMetadata, exception) -> {
//            recordMetadata.offset()
//        });

//SYNCHRONOUS
//        try {
//            simplestProducer.send(message).get();
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        }

        simplestProducer.close();
    }
}
