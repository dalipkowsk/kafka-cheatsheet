package simplest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class SimplestHeadersConsumer {

    public static void main(String[] args) {
        System.out.println("Simplest Consumer - reading headers");

        final Properties properties = new Properties();
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::close));
        kafkaConsumer.assign(kafkaConsumer.partitionsFor("simplest-headers-topic")
                .stream()
                .map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
                .toList());

        while(true) {
            ConsumerRecords<String, String> polledRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            polledRecords.records("simplest-headers-topic").forEach(record -> {
                System.out.println("Message from simplest-topic: " + record.value());
                System.out.println("Their headers: " + record.headers());
            });

            //We do not want to commit, because we do not want to reset offsets everytime!
        }
    }
}
