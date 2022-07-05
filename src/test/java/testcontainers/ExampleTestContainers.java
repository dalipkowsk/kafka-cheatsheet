package testcontainers;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Properties;

/**
 * Integration test with TestContainers.
 */
@Testcontainers
public class ExampleTestContainers {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))
            .withEmbeddedZookeeper();

    @BeforeAll
    static void setup(){
        try {
            org.testcontainers.containers.Container.ExecResult execResult = kafka.execInContainer("sh", "-c", "kafka-topics --create --topic random-topic --partitions 4 --bootstrap-server localhost:9092");
            System.out.println(execResult.toString());
        } catch (IOException | InterruptedException e) {
            System.out.println("Error during executing command in container");
        }
    }

    @Test
    void testSimpleKafkaProducer() {
        //Producer
        final Properties producerProperties = new Properties();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final Producer<String, String> kafkaProducer = new KafkaProducer<>(producerProperties);
        ProducerRecord<String, String> kafkaMessage = new ProducerRecord<>("test-topic", "Our first message with Kafka Containers");
        kafkaProducer.send(kafkaMessage);
        kafkaProducer.close();

        //Consumer
        final Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties);
        consumer.subscribe(List.of("test-topic"));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(30000));
        records.records("test-topic")
                .forEach(message -> System.out.println(message.offset() + " | " + message.partition() + " | " +
                        Instant.ofEpochMilli(message.timestamp()) + " | " + message.value()));
        consumer.close();
    }
}
