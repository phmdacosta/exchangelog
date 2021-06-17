package com.pedrocosta.exchangelog.persistence;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    /**
     * Search in database for exchange by its base currency, quote currency and value date.
     *
     * @param baseCcy   Exchange's base currency
     * @param quoteCcy  Exchange's quote currency
     * @param valueDate Exchange's value date
     *
     * @return Found {@link Exchange} instance.
     */
    Exchange findByBaseCurrencyAndQuoteCurrencyAndValueDate(Currency baseCcy, Currency quoteCcy, Date valueDate);

    /**
     * Search in database for exchanges by its base currency and quote values
     * order by descending value date.
     *
     * @param baseCcy   Exchange's base currency
     * @param quoteCcy  Exchange's quote currency
     * @return The most current found {@link Exchange} instance.
     */
    Exchange findFirstByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(Currency baseCcy, Currency quoteCcy);

    /**
     * Search in database for exchanges with value date between a specific period
     * order by ascending rate.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     * @return Exchange with minimum rate.
     */
    Exchange findByValueDateBetweenOrderByRateAsc(Date valueDateStart, Date valueDateEnd);

    /**
     * Search in database for exchanges with value date between a specific period
     * order by descending rate.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     * @return Exchange with maximum rate.
     */
    Exchange findByValueDateBetweenOrderByRateDesc(Date valueDateStart, Date valueDateEnd);

    /**
     * Search in database for exchanges by its base currency and value date.
     *
     * @param baseCcy   Exchange's base currency
     * @param valueDate Exchange's value date
     *
     * @return List of found {@link Exchange} instance.
     */
    List<Exchange> findAllByBaseCurrencyAndValueDate(Currency baseCcy, Date valueDate);

    /**
     * Search in database for exchanges with value date between a specific period.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     *
     * @return List of found {@link Exchange} instance.
     */
    List<Exchange> findAllByValueDateBetween(Date valueDateStart, Date valueDateEnd);

    /**
     * Search in database for exchanges with value date between a specific period
     * order by ascending rate.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     * @return Exchange with minimum rate.
     */
    List<Exchange> findAllByValueDateBetweenOrderByRateAsc(Date valueDateStart, Date valueDateEnd);

    /**
     * Search in database for exchanges with value date between a specific period
     * order by descending rate.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     * @return Exchange with maximum rate.
     */
    List<Exchange> findAllByValueDateBetweenOrderByRateDesc(Date valueDateStart, Date valueDateEnd);

    /**
     * Search in database for exchanges with value date between a specific period
     * order by descending value date.
     *
     * @param valueDateStart    Start date
     * @param valueDateEnd      End date
     * @return Exchange ordered by value date.
     */
    List<Exchange> findAllByValueDateBetweenOrderByValueDateDesc(Date valueDateStart, Date valueDateEnd);
}
