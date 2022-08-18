package admintools;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DeleteTopicsResult;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class DeleteTopic {
    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (Admin admin = Admin.create(properties)) {
            DeleteTopicsResult deleteTopicsResult = admin.deleteTopics(List.of("bank-transaction-topic"));

            deleteTopicsResult.all().get();
        } catch (ExecutionException e) {
            System.out.println("Something wrong! " + e.getMessage());
        } catch (InterruptedException f) {
            Thread.currentThread().interrupt();
            System.out.println("Interrupted! " + f.getMessage());
        }
    }
}

