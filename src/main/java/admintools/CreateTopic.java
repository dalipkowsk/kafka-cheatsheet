package admintools;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.KafkaFuture;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class CreateTopic {
    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (Admin admin = Admin.create(properties)) {
            int partitionsNo = 1;
            short replicationFactor = 1;

            final NewTopic newTopic = new NewTopic("bank-transaction-topic", partitionsNo, replicationFactor);
            CreateTopicsResult createTopicsResult = admin.createTopics(List.of(newTopic));

            final KafkaFuture<Config> future = createTopicsResult.config("bank-transaction-topic");
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
