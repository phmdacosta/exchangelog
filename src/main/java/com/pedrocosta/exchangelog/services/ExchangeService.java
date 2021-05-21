package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
import com.pedrocosta.exchangelog.utils.DatabaseOrder;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeService implements RepositoryService<Exchange> {

    private ExchangeRepository repository;

    public ExchangeService(ExchangeRepository repository) {
        this.repository = repository;
    }

    /**
     * Find a exchanges based on it ID.
     *
     * @param id      ID of exchange
     *
     * @return List of existing {@link Exchange} object.
     */
    @Override
    @Nullable
    public Exchange find(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Find a exchanges based on parameters in database.
     *
     * @param ccy      Base currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return List of existing {@link Exchange} object.
     */
    public List<Exchange> find(Currency ccy, Date valueDate) {
        return repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate);
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return Existing {@link Exchange} object.
     */
    public Exchange find(Currency ccy1, Currency ccy2, Date valueDate) {
        return repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
                ccy1, ccy2, valueDate);
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     *
     * @return Existing {@link Exchange} object.
     */
    public Exchange findLast(Currency ccy1, Currency ccy2) {
        return repository.findByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
                ccy1, ccy2);
    }

    /**
     * Find exchange with minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @return Exchange with minimum rate.
     */
    public Exchange findWithMinRate(Date startValueDate, Date endValueDate) {
        return repository.findByValueDateBetweenOrderByRateAsc(startValueDate, endValueDate);
    }

    /**
     * Find exchange with maximum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @return Exchange with maximum rate.
     */
    public Exchange findWithMaxRate(Date startValueDate, Date endValueDate) {
        return repository.findByValueDateBetweenOrderByRateDesc(startValueDate, endValueDate);
    }

    /**
     * List all exchanges from database.
     * @return List of all saved exchanges
     */
    @Override
    public List<Exchange> findAll() {
        return repository.findAll();
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @return List of all saved exchanges
     */
    public List<Exchange> findAll(Date startValueDate, Date endValueDate) {
        return repository.findAllByValueDateBetween(startValueDate, endValueDate);
    }

    /**
     * Find all exchanges ordered by minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @param order             Set if order is ascending or decreasing,
     *                          if null it will be considered ascending
     * @return List of all saved exchanges order by rate
     */
    public List<Exchange> findAllOrderedByRate(Date startValueDate, Date endValueDate, DatabaseOrder order) {
        if (DatabaseOrder.DESC == order) {
            return repository.findAllByValueDateBetweenOrderByRateDesc(startValueDate, endValueDate);
        }

        return repository.findAllByValueDateBetweenOrderByRateAsc(startValueDate, endValueDate);
    }

    /**
     * Save a {@link Exchange} object.
     *
     * @param exchange Exchange to save
     *
     * @return Saved exchange.
     */
    @Override
    public Exchange save(Exchange exchange) {
        return repository.save(exchange);
    }

    /**
     * Save a new {@link Exchange} object.
     * If it already exists, update it.
     *
     * @param ccy1      Base currency
     * @param ccy2      Quote currency
     * @param rate      Rate value
     * @param valueDate Valuation date of the rate
     *
     * @return Saved {@link Exchange} object.
     */
    public Exchange save(Currency ccy1, Currency ccy2, BigDecimal rate, Date valueDate) {
        Exchange exchange = find(ccy1, ccy2, valueDate);
        if (exchange == null) {
            exchange = new Exchange();
            exchange.setBaseCurrency(ccy1);
            exchange.setQuoteCurrency(ccy2);
            exchange.setValueDate(valueDate);
        }
        exchange.setRate(rate);
        return save(exchange);
    }

    public Exchange save(Currency ccy1, Currency ccy2, Double rate, Date valueDate) {
        return save(ccy1, ccy2, BigDecimal.valueOf(rate), valueDate);
    }

    /**
     * Save all {@link Exchange} objects in a list.
     *
     * @param exchanges List of exchanges to save.
     *
     * @return List of saved exchanges.
     */
    @Override
    public List<Exchange> saveAll(Collection<Exchange> exchanges) {
        return repository.saveAll(exchanges);
    }
}
