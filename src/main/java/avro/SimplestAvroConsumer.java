package avro;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * It requires:
 * single-kafka-cluster
 * schema-registry
 *
 * Topic: simplest-avro-topic
 */
public class SimplestAvroConsumer {
    public static void main(String[] args) {
        System.out.println("Simplest Avro Consumer");

        final Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "simplest-avro-consumer-group");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", "http://127.0.0.1:8081");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        final KafkaConsumer<String, GenericRecord> kafkaConsumer = new KafkaConsumer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::close));
        kafkaConsumer.subscribe(List.of("simplest-avro-topic"));

        while(true) {
            ConsumerRecords<String, GenericRecord> polledRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            polledRecords.records("simplest-avro-topic").forEach(record -> {
                System.out.println("Message from simplest-avro-topic: " + record.value());
            });
        }
    }
}
