package exactlyonce;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Transaction coordinator
 *
 * LSO - last stable offset
 *
 * Internal topics
 * __transaction_state-0
 * __transaction_state-1
 * ...
 *
 * Indempotent Producer guarantees exactly-once delivery within producer lifetime and per partition level
 * When producer crashes "exactly one" will fail
 *
 * Transactional Producer is an expanded version of indempotent producer.
 * https://chrzaszcz.dev/2019/12/kafka-transactions/
 *
 */
public class TransactionalProducer {
    public static void main(String[] args) {
        System.out.println("Simplest Producer");

        final Properties properties = new Properties();
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "payments");
        properties.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 30000);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        final ProducerRecord<String, String> message = new ProducerRecord<>("transactional-topic", "Payment 0 - 250");

        final KafkaProducer<String, String> simplestProducer = new KafkaProducer<>(properties);
        simplestProducer.initTransactions();
        simplestProducer.beginTransaction();
        try {
            RecordMetadata recordMetadata = simplestProducer.send(message).get();   //block
            System.out.println("payment timestamp: " + recordMetadata.timestamp());
            Thread.sleep(10000);
            simplestProducer.commitTransaction();
        } catch (InterruptedException | ExecutionException e) {
            simplestProducer.abortTransaction();
            System.out.println("Transaction was aborted!");
            e.printStackTrace();
        }

        simplestProducer.close();
    }
}
