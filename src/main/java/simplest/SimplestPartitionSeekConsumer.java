package simplest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class SimplestPartitionSeekConsumer {

    public static void main(String[] args) {
        System.out.println("Simplest Consumer");

        final Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaConsumer::close));

        kafkaConsumer.partitionsFor("simplest-topic")
                .forEach(System.out::println);

        TopicPartition topicPartition = new TopicPartition("simplest-topic", 2);
        kafkaConsumer.assign(List.of(topicPartition));
        kafkaConsumer.seek(topicPartition, 0);

        while(true) {
            ConsumerRecords<String, String> polledRecords = kafkaConsumer.poll(Duration.ofSeconds(1));

            polledRecords.records("simplest-topic")
                    .forEach(record -> System.out.println(record.offset() + " : " + record.timestamp() + " : " + record.value()));
            //kafkaConsumer.commitSync();
        }
    }
}
