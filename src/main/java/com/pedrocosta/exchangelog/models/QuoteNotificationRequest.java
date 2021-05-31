package com.pedrocosta.exchangelog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity(name = "quote_not_req")
public class QuoteNotificationRequest extends NotificationRequest implements Cloneable {

    @Column(name = "quote_value")
    private BigDecimal quoteValue;
    @Column(name = "logical_operator")
    private String logicalOperator;
    @ManyToOne
    private Exchange exchange;
    @Column(name = "exch_start_date")
    private Date exchStartDate;
    @Column(name = "exch_end_date")
    private Date exchEndDate;
    private int period;
    @Column(name = "period_type")
    private String periodType;

    public BigDecimal getQuoteValue() {
        return quoteValue;
    }

    public QuoteNotificationRequest setQuoteValue(BigDecimal quoteValue) {
        this.quoteValue = quoteValue;
        return this;
    }

    public QuoteNotificationRequest setQuoteValue(double quoteValue) {
        return setQuoteValue(BigDecimal.valueOf(quoteValue));
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public QuoteNotificationRequest setLogicalOperator(String logicalOperator) {
        this.logicalOperator = logicalOperator;
        return this;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public QuoteNotificationRequest setExchange(Exchange exchange) {
        this.exchange = exchange;
        return this;
    }

    public Date getExchStartDate() {
        return exchStartDate;
    }

    public QuoteNotificationRequest setExchStartDate(Date exchStartDate) {
        this.exchStartDate = exchStartDate;
        return this;
    }

    public Date getExchEndDate() {
        return exchEndDate;
    }

    public QuoteNotificationRequest setExchEndDate(Date exchEndDate) {
        this.exchEndDate = exchEndDate;
        return this;
    }

    public int getPeriod() {
        return period;
    }

    public QuoteNotificationRequest setPeriod(int period) {
        this.period = period;
        return this;
    }

    public String getPeriodType() {
        return periodType;
    }

    public QuoteNotificationRequest setPeriodType(String periodType) {
        this.periodType = periodType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuoteNotificationRequest that = (QuoteNotificationRequest) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getMeans(), getQuoteValue(), getLogicalOperator());
    }

    @Override
    protected QuoteNotificationRequest clone() throws CloneNotSupportedException {
        QuoteNotificationRequest cloned = (QuoteNotificationRequest) super.clone();
        cloned.setQuoteValue(this.getQuoteValue())
                .setLogicalOperator(this.getLogicalOperator())
                .setExchange(this.getExchange().clone())
                .setExchStartDate(this.getExchStartDate())
                .setExchEndDate(this.getExchEndDate());
        return cloned;
    }
}
