package restproxy;

import java.io.Serializable;

public class RestProxyProducerRecord implements Serializable {
    private final String key;
    private final String value;

    public RestProxyProducerRecord(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
