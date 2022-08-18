package custom;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class BankTransactionConsumer {

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "simplest-consumer-group");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BankTransactionDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final KafkaConsumer<String, BankTransaction> kafkaConsumer = new KafkaConsumer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::close));
        kafkaConsumer.subscribe(List.of("bank-transaction-topic"));

        while(true) {
            ConsumerRecords<String, BankTransaction> polledRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            polledRecords.records("bank-transaction-topic").forEach(record -> {
                System.out.println(record.value());
            });
        }
    }
}
