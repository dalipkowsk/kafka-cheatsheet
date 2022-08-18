package admintools;

import org.apache.kafka.clients.admin.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ListTopics {
    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (Admin admin = Admin.create(properties)) {
            final ListTopicsResult listTopicsResult = admin.listTopics();
            listTopicsResult.listings().get().forEach(topic -> System.out.println(topic));
        } catch (ExecutionException e) {
            System.out.println("Error! " + e.getMessage());
        } catch (InterruptedException f) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted! " + f.getMessage());
        }
    }
}
