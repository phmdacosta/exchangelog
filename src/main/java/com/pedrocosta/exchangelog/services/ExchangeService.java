package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
import com.pedrocosta.exchangelog.utils.DatabaseOrder;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeService implements RepositoryService<Exchange> {

    private final ExchangeRepository repository;

    public ExchangeService(ExchangeRepository repository) {
        this.repository = repository;
    }

    /**
     * Find a exchanges based on it ID.
     *
     * @param id ID of exchange
     *
     * @return  {@link ServiceResponse} object with found exchange.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<Exchange> find(long id) {
        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findById(id).orElse(null));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = "with id " + id;
            result.setMessage(Messages.get("error.exch.not.found", arg));
        }

        return result;
    }

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  {@link ServiceResponse} object with found exchanges.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<List<Exchange>> find(@NotNull Currency ccy, Date valueDate) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = "of " + ccy.getCode() + " in " + valueDate;
            result.setMessage(Messages.get("error.exch.not.found", arg));
        }

        return result;
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  {@link ServiceResponse} object with found exchange.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Exchange> find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate) {
        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
                ccy1, ccy2, valueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = ccy1.getCode() + "/" + ccy2.getCode() + " in " + valueDate;
            result.setMessage(Messages.get("error.exch.not.found", arg));
        }

        return result;
    }

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     *
     * @return  {@link ServiceResponse} object with found exchange.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Exchange> findLast(@NotNull Currency ccy1, @NotNull Currency ccy2) {
        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
                ccy1, ccy2));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = ccy1.getCode() + "/" + ccy2.getCode();
            result.setMessage(Messages.get("error.exch.not.found", arg));
        }

        return result;
    }

    /**
     * Find exchange with minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  {@link ServiceResponse} object with found exchange.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Exchange> findWithMinRate(Date startValueDate, Date endValueDate) {
        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByValueDateBetweenOrderByRateAsc(
                startValueDate, endValueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.exch.not.found.in.dates",
                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
        }

        return result;
    }

    /**
     * Find exchange with maximum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  {@link ServiceResponse} object with found exchange.
     *          If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Exchange> findWithMaxRate(Date startValueDate, Date endValueDate) {
        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByValueDateBetweenOrderByRateDesc(
                startValueDate, endValueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.exch.not.found.in.dates",
                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
        }

        return result;
    }

    /**
     * List all exchanges from database.
     *
     * @return {@link ServiceResponse} object with list of found exchanges.
     *         If not found, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<Exchange>> findAll() {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAll());

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.no.exch.found"));
        }

        return result;
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return {@link ServiceResponse} object with list of found exchanges.
     *         If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<List<Exchange>> findAll(Date startValueDate, Date endValueDate) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAllByValueDateBetween(startValueDate, endValueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.exch.not.found.in.dates",
                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
        }

        return result;
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return {@link ServiceResponse} object with list of found exchanges.
     *         If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<List<Exchange>> findAllOrderByValueDate(Date startValueDate, Date endValueDate) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAllByValueDateBetweenOrderByValueDateDesc(
                startValueDate, endValueDate));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.exch.not.found.in.dates",
                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
        }

        return result;
    }

    /**
     * Find all exchanges ordered by minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @param order             Set if order is ascending or decreasing,
     *                          if null it will be considered ascending
     *
     * @return {@link ServiceResponse} object with list of found exchanges.
     *         If not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<List<Exchange>> findAllOrderedByRate(Date startValueDate, Date endValueDate, DatabaseOrder order) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.OK);

        if (DatabaseOrder.DESC == order) {
            result.setObject(repository.findAllByValueDateBetweenOrderByRateDesc(
                    startValueDate, endValueDate));
        } else {
            result.setObject(repository.findAllByValueDateBetweenOrderByRateAsc(
                    startValueDate, endValueDate));
        }

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.exch.not.found.in.dates",
                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
        }

        return result;
    }

    /**
     * Save a {@link Exchange} object.
     *
     * @param exchange Exchange to save
     *
     * @return {@link ServiceResponse} object with saved exchange.
     *         If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<Exchange> save(Exchange exchange) {
        if (exchange.getBaseCurrency() == null) {
            return new ServiceResponse<Exchange>(HttpStatus.BAD_REQUEST)
                    .setMessage(Messages.get("error.exch.save.no.ccy.set", "base"));
        }
        if (exchange.getQuoteCurrency() == null) {
            return new ServiceResponse<Exchange>(HttpStatus.BAD_REQUEST)
                    .setMessage(Messages.get("error.exch.save.no.ccy.set", "quote"));
        }

        ServiceResponse<Exchange> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.save(exchange));

        if (result.getObject() == null || result.getObject().getId() == 0) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            result.setMessage(Messages.get("error.exch.not.saved",
                    exchange.getBaseCurrency().getCode(), exchange.getQuoteCurrency().getCode()));
        }

        return result;
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
     * @return {@link ServiceResponse} object with saved exchange.
     *         If error, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Exchange> save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          BigDecimal rate, @NotNull Date valueDate) {
        return save(new Exchange(ccy1, ccy2, rate, valueDate));
    }

    public ServiceResponse<Exchange> save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          Double rate,  @NotNull Date valueDate) {
        return save(ccy1, ccy2, BigDecimal.valueOf(rate), valueDate);
    }

    /**
     * Save all {@link Exchange} objects in a list.
     *
     * @param exchanges List of exchanges to save.
     *
     * @return {@link ServiceResponse} object with list of saved exchanges.
     *         If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<Exchange>> saveAll(Collection<Exchange> exchanges) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.saveAll(exchanges));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            result.setMessage(Messages.get("error.not.saved", "any exchange"));
        }
        else if(result.getObject().size() != exchanges.size()) {
            List<Exchange> notSaved = new ArrayList<>();
            ServiceResponse<List<Exchange>> auxResult = result.setCode(HttpStatus.BAD_REQUEST);
            exchanges.forEach(exchange -> {
                if (!auxResult.getObject().contains(exchange)) {
                    notSaved.add(exchange);
                }
            });
            String msg = Messages.get("error.some.exch.not.saved", notSaved.toString());
            result.setMessage(msg);
        }

        return result;
    }
}
