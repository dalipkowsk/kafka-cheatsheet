package custom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Example use of custom serializers/deserializers
 */
public class BankTransactionProducer {
    private final List<String> clients = List.of("Client A", "Client B", "Client C", "Client D", "Client E", "Client F",
            "Client G", "Client H", "Client I", "Client J", "Client K");
    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "simplest-producer");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BankTransactionSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "1");

        final KafkaProducer<String, BankTransaction> simplestProducer = new KafkaProducer<>(properties);
        final BankTransactionProducer bankTransactionProducer = new BankTransactionProducer();
        for(int i = 0; i < 10000; i++) {
            final BankTransaction bankTransaction = bankTransactionProducer.generateRandomBankTransaction();
            System.out.println(bankTransaction);
            final ProducerRecord<String, BankTransaction> message = new ProducerRecord<>("bank-transaction-topic", bankTransaction);
            Future<RecordMetadata> send = simplestProducer.send(message);
        }
    }

    private BankTransaction generateRandomBankTransaction(){
        int userIdA = threadLocalRandom.nextInt(0, 10);
        int userIdB = threadLocalRandom.nextInt(0, 10);
        final BigDecimal bigDecimal = randomBigDecimal(BigDecimal.valueOf(50000));

        return new BankTransaction(Instant.now(), clients.get(userIdA), clients.get(userIdB), bigDecimal);
    }

    private BigDecimal randomBigDecimal(final BigDecimal max) {
        final BigDecimal bigDecimal = BigDecimal.valueOf(threadLocalRandom.nextDouble());
        return bigDecimal.multiply(max).setScale(2, RoundingMode.DOWN);
    }
}
