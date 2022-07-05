package simplest;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimplestMultithreadedConsumer {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.submit(new SimplestTaskConsumer("consumer-task-0"));
        executorService.submit(new SimplestTaskConsumer("consumer-task-1"));

        //Work for 1 minute
        Thread.sleep(60000);
        executorService.shutdownNow();
    }

    public static class SimplestTaskConsumer implements Runnable {
        private final AtomicBoolean closed = new AtomicBoolean(false);
        private final Consumer<String, String> consumer;

        public SimplestTaskConsumer(String consumerName){
            final Properties consumerProperties = new Properties();
            consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumerName);
            consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
            consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "simplest-consumer-group");
            consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            this.consumer = new KafkaConsumer<>(consumerProperties);
        }

        @Override
        public void run() {
            try {
                consumer.subscribe(List.of("simplest-topic"));
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
                    records.records("simplest-topic")
                            .forEach(message -> System.out.println(message.offset() + "|" + message.partition() + "|" + Instant.ofEpochMilli(message.timestamp()) + "|" + message.value()));
                }
            } catch (WakeupException e) {
                if (!closed.get()) throw e;
            } finally {
                consumer.close();
            }
        }

        public void shutdown() {
            closed.set(true);
            consumer.wakeup();
        }
    }
}
