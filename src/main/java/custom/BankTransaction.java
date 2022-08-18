package custom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Model used for bank transactions.
 * https://www.educba.com/kafka-replication/
 */
public class BankTransaction implements Serializable {
    @Serial
    private static final long serialVersionUID = -7320349240094102434L;

    //@JsonFormat(pattern = "yyyy-MM-ddTHH:mm:ss", timezone = "UTC")
    private final Instant timestamp;
    private final String from;
    private final String to;
    private final BigDecimal amount;

    @JsonCreator
    public BankTransaction(
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("from") String from,
            @JsonProperty("to") String to,
            @JsonProperty("amount") BigDecimal amount) {
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BankTransaction{" +
                "timestamp=" + timestamp +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                '}';
    }
}
