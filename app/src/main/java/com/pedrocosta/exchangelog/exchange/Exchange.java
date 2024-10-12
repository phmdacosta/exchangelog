package com.pedrocosta.exchangelog.exchange;

import com.pedrocosta.exchangelog.currency.Currency;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
		columnNames = {
				"base_currency_id",
				"quote_currency_id",
				"value_date"
		})
)
public class Exchange implements Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	private Currency baseCurrency;

	@OneToOne(fetch = FetchType.LAZY)
	private Currency quoteCurrency;

	@Column(name = "value_date")
	private Date valueDate;
	private BigDecimal rate;

	public Exchange() {
		this(null, null, BigDecimal.ZERO, null);
	}

	public Exchange(Currency base, Currency quote, BigDecimal rate, Date date) {
		setBaseCurrency(base);
		setQuoteCurrency(quote);
		setRate(rate);
		setValueDate(date);
	}

	public long getId() {
		return id;
	}

	public Exchange setId(long id) {
		this.id = id;
		return this;
	}

	public Currency getBaseCurrency() {
		return baseCurrency;
	}

	public Exchange setBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
		return this;
	}

	public Currency getQuoteCurrency() {
		return quoteCurrency;
	}

	public Exchange setQuoteCurrency(Currency quoteCurrency) {
		this.quoteCurrency = quoteCurrency;
		return this;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public Exchange setValueDate(Date valueDate) {
		this.valueDate = valueDate;
		return this;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public Exchange setRate(BigDecimal rate) {
		this.rate = rate;
		return this;
	}

	public Exchange setRate(Double rate) {
		return setRate(BigDecimal.valueOf(rate));
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, baseCurrency, quoteCurrency, valueDate, rate);
	}

	@Override
	public Exchange clone() throws CloneNotSupportedException {
		return(Exchange) super.clone();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Exchange{")
				.append("id=").append(id)
				.append(", baseCurrency=");

		if (baseCurrency != null)
			builder.append(baseCurrency.getCode());

		builder.append(", baseCurrency=");
		if (quoteCurrency != null)
			builder.append(quoteCurrency.getCode());

		builder.append(", valueDate=").append(valueDate)
				.append(", rate=").append(rate)
				.append('}');

		return builder.toString();
	}
}
