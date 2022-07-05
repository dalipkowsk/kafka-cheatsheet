package exactlyonce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group transactional-consumer-group --describe
 * kafka-consumer-groups.bat --bootstrap-server localhost:9092 --group transactional-consumer-group --reset-offsets  --topic transactional-topic  --to-earliest --execute
 *
 * https://stackoverflow.com/questions/57321763/kafka-producer-idempotence-exactly-once-or-just-producer-transaction-is-enough
 */
public class IsolationLevelConsumer {

    public static void main(String[] args) {
        System.out.println("Simplest Consumer");

        final Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "transactional-consumer-group");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");  //read_committed read_uncommitted

        final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::close));
        kafkaConsumer.subscribe(List.of("transactional-topic"));

        while(true) {
            ConsumerRecords<String, String> polledRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            polledRecords.records("transactional-topic").forEach(record -> {
                System.out.println("Message from simplest-topic: " + record.value());
            });
        }
    }
}
