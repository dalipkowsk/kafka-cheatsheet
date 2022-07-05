package unit;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.record.TimestampType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExampleKafkaUnitTests {

    @Test
    void exampleSimplestMockedProducer() {
        final MockProducer<String, String> mockProducer = new MockProducer<>(true, new StringSerializer(), new StringSerializer());

        final ProducerRecord<String, String> message = new ProducerRecord<>("simplest-kafka-topic", "mocked-message-from-producer");
        try {
            mockProducer.send(message).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        mockProducer.history().forEach(x -> System.out.println(x));
    }

    @Test
    public void exampleSimplestMockedConsumer(){
        final MockConsumer<String, String> mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        mockConsumer.schedulePollTask(() -> {
            mockConsumer.rebalance(Collections.singletonList(new TopicPartition("the_topic", 0)));
            mockConsumer.addRecord(new ConsumerRecord<>("the_topic", 0, 0L, 0L, TimestampType.NO_TIMESTAMP_TYPE, 0L, 0, 0, "my-key", "mocked-message",
                    new RecordHeaders(List.of(new RecordHeader("header_key", "header_value".getBytes())))));
            mockConsumer.seek(new TopicPartition("the_topic", 0), 0L);
        });

        mockConsumer.subscribe(List.of("the_topic"));

        final ConsumerRecords<String, String> poll = mockConsumer.poll(Duration.ofSeconds(5));
        poll.records("the_topic").forEach(message -> System.out.println("Message from mocked producer: " + message.value()));
    }
}
