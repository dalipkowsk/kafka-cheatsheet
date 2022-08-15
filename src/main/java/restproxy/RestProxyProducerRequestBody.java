package restproxy;

import java.io.Serializable;
import java.util.List;

public class RestProxyProducerRequestBody implements Serializable {
    private List<RestProxyProducerRecord> records;

    public RestProxyProducerRequestBody(List<RestProxyProducerRecord> records) {
        this.records = records;
    }

    public List<RestProxyProducerRecord> getRecords() {
        return records;
    }
}
