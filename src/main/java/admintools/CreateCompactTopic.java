package admintools;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.config.TopicConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CreateCompactTopic {

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (Admin admin = Admin.create(properties)) {
            int partitionsNo = 1;
            short replicationFactor = 1;

            //Add some additional properties
            final Map<String, String> newTopicConfig = new HashMap<>();
            newTopicConfig.put(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_COMPACT);
            newTopicConfig.put(TopicConfig.COMPRESSION_TYPE_CONFIG, "snappy");

            final NewTopic newTopic = new NewTopic("compact-topic", partitionsNo, replicationFactor)
                    .configs(newTopicConfig);;
            CreateTopicsResult createTopicsResult = admin.createTopics(List.of(newTopic));

            final KafkaFuture<Config> future = createTopicsResult.config("compact-topic");
            Config config = future.get();

            config.entries().forEach(entry -> System.out.println(entry));
        } catch (ExecutionException e) {
            System.out.println("Something wrong! " + e.getMessage());
        } catch (InterruptedException f) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted! " + f.getMessage());
        }
    }
}
