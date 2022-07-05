package avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * It requires:
 * single-kafka-cluster
 * schema-registry
 */
public class SimplestAvroProducer {
    public static void main(String[] args) {
        System.out.println("Simplest Avro Producer");

        final Properties properties = new Properties();
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "simplest-avro-producer");
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put("schema.registry.url", "http://127.0.0.1:8081");

        //It is usually stored as *.avsc file in resources
        String schemaString = """
        {
            "namespace":"customerManagement.avro",
            "type":"record",
            "name":"Customer",
            "fields":[
                {
                    "name":"id",
                    "type":"int"
                },
                {
                    "name":"name",
                    "type":"string"
                },
                {
                    "name":"email",
                    "type":"string",
                    "default":"null"
                }
            ]
        }
        """;

        final Producer<String, GenericRecord> producer = new KafkaProducer<>(properties);

        final Schema.Parser parser = new Schema.Parser();
        final Schema schema = parser.parse(schemaString);

        for (int nCustomers = 0; nCustomers < 10; nCustomers++) {
            final GenericRecord customer = new GenericData.Record(schema);
            String name = "exampleCustomer" + nCustomers;
            String email = "example" + nCustomers + "@example.com";
            customer.put("id", nCustomers);
            customer.put("surname", "xaxa");
            customer.put("name", name);
            customer.put("email", email);

            ProducerRecord<String, GenericRecord> data = new ProducerRecord<>("simplest-avro-topic", name, customer);
            producer.send(data);
        }

        producer.close();
    }
}
