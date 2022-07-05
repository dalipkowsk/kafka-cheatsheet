package model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Model used for bank transactions.
 */
public class BankTransaction {
    private final Instant timestamp;
    private final String from;
    private final String to;
    private final BigDecimal amount;

    public BankTransaction(Instant timestamp, String from, String to, BigDecimal amount) {
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
}
