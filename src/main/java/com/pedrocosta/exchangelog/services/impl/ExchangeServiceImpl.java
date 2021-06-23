package com.pedrocosta.exchangelog.services.impl;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
import com.pedrocosta.exchangelog.services.CurrencyService;
import com.pedrocosta.exchangelog.services.ExchangeService;
import com.pedrocosta.exchangelog.services.ServiceFactory;
import com.pedrocosta.exchangelog.utils.DatabaseOrder;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final ExchangeRepository repository;
    private final ServiceFactory serviceFactory;

    public ExchangeServiceImpl(ExchangeRepository repository, ServiceFactory serviceFactory) {
        this.repository = repository;
        this.serviceFactory = serviceFactory;
    }

    /**
     * Find a exchanges based on it ID.
     *
     * @param id ID of exchange
     * @return  Found exchange. Null if not found.
     */
    @Override
    @Nullable
    public Exchange find(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public List<Exchange> find(@NotNull Currency ccy, Date valueDate) {
        handleCurrency(ccy);
        return repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate);
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate) {
        return find(ccy1, ccy2, valueDate, null);
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate, double amount) {
        return find(ccy1, ccy2, valueDate, BigDecimal.valueOf(amount));
    }

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate, BigDecimal amount) {
        handleCurrency(ccy1);
        handleCurrency(ccy2);

        Exchange exchange =  repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
                ccy1, ccy2, valueDate);
        if (amount != null && !BigDecimal.ZERO.equals(amount)) {
            exchange.setRate(exchange.getRate().multiply(amount));
        }
        return exchange;
    }

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2) {
        return findLast(ccy1, ccy2, null);
    }

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param amount    Amount of ccy1.
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2, double amount) {
        return findLast(ccy1, ccy2, BigDecimal.valueOf(amount));
    }

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param amount    Amount of ccy1.
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2, BigDecimal amount) {
        handleCurrency(ccy1);
        handleCurrency(ccy2);
        Exchange exchange = repository.findFirstByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
                ccy1, ccy2);
        if (amount != null && !BigDecimal.ZERO.equals(amount)) {
            exchange.setRate(exchange.getRate().multiply(amount));
        }
        return exchange;
    }

    /**
     * Find exchange with minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange findWithMinRate(Date startValueDate, Date endValueDate) {
        return repository.findByValueDateBetweenOrderByRateAsc(
                startValueDate, endValueDate);
    }

    /**
     * Find exchange with maximum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public Exchange findWithMaxRate(Date startValueDate, Date endValueDate) {
        return repository.findByValueDateBetweenOrderByRateDesc(
                startValueDate, endValueDate);
    }

    /**
     * List all exchanges from database.
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    @Override
    public List<Exchange> findAll() {
        return repository.findAll();
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param base  Base currency of exchange
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    public List<Exchange> findAll(Currency base) {
        return findAll(base, null, null);
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param base      Base currency of exchange
     * @param amount    Amount of base currency
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    public List<Exchange> findAll(Currency base, double amount) {
        return findAll(base, null, BigDecimal.valueOf(amount));
    }

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public List<Exchange> findAll(@NotNull Currency ccy, Date valueDate, double amount) {
        return findAll(ccy, valueDate, BigDecimal.valueOf(amount));
    }

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable
    public List<Exchange> findAll(@NotNull Currency ccy, Date valueDate, BigDecimal amount) {
        handleCurrency(ccy);

//        if (base == null) {
//            return new ArrayList<>();
//        }

        List<Exchange> result;

        if (valueDate != null) {
            result = repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate);
        } else {
            result = repository.findAllByBaseCurrency(ccy);
        }

        if (amount != null && !BigDecimal.ZERO.equals(amount)) {
            for (Exchange exchange : result) {
                exchange.setRate(exchange.getRate().multiply(amount));
            }
        }

        return result;
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    public List<Exchange> findAll(Date startValueDate, Date endValueDate) {
        return repository.findAllByValueDateBetween(
                startValueDate, endValueDate);
    }

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return List of found exchanges. Empty list if not found.
     */
    public List<Exchange> findAllOrderByValueDate(Date startValueDate, Date endValueDate) {
        return repository.findAllByValueDateBetweenOrderByValueDateDesc(
                startValueDate, endValueDate);
    }

    /**
     * Find all exchanges ordered by minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     * @param order             Set if order is ascending or decreasing,
     *                          if null it will be considered ascending
     *
     * @return List of found exchanges. Empty list if not found.
     */
    public List<Exchange> findAllOrderedByRate(Date startValueDate, Date endValueDate, DatabaseOrder order) {
        List<Exchange> result;

        if (DatabaseOrder.DESC == order) {
            result = repository.findAllByValueDateBetweenOrderByRateDesc(
                    startValueDate, endValueDate);
        } else {
            result = repository.findAllByValueDateBetweenOrderByRateAsc(
                    startValueDate, endValueDate);
        }

        return result;
    }

    /**
     * Save a {@link Exchange} object.
     * <br>
     * This method will search for currencies id if it's not filled
     * and checks if exchange already exists in database with same
     * base currency, quote currency and value date.
     * <br>
     * If exchange's id is not zero, it will update this data.
     *
     * @param exchange Exchange to save
     *
     * @return Saved exchange.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override
    @Transactional
    public Exchange save(Exchange exchange) throws SaveDataException {
        try {
            CurrencyService ccyServ = (CurrencyService) serviceFactory.create(Currency.class);
            if (exchange.getBaseCurrency().getId() == 0) {
                Currency baseCcy = ccyServ.find(exchange.getBaseCurrency().getCode());
                if (baseCcy == null) {
                    throw new NoSuchDataException(
                            Messages.get("error.ccy.not.found",
                                    exchange.getBaseCurrency().getCode()));
                }
                exchange.getBaseCurrency().setId(baseCcy.getId());
            }
            if (exchange.getQuoteCurrency().getId() == 0) {
                Currency quoteCcy = ccyServ.find(exchange.getQuoteCurrency().getCode());
                if (quoteCcy == null) {
                    throw new NoSuchDataException(
                            Messages.get("error.ccy.not.found",
                                    exchange.getQuoteCurrency().getCode()));
                }
                exchange.getQuoteCurrency().setId(quoteCcy.getId());
            }
        } catch (NoSuchDataException | NullPointerException e) {
            throw new SaveDataException(Messages.get("error.exch.save.wrong.ccy"), e);
        }

        if (exchange.getId() == 0) {
            // Search for existing exchange
            Exchange dbExch = repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
                    exchange.getBaseCurrency(), exchange.getQuoteCurrency(),
                    exchange.getValueDate());
            // If exchange exists, we get its id to update
            if (dbExch != null) {
                exchange.setId(dbExch.getId());
            }
        }

        // Execute save
        Exchange result = repository.save(exchange);
        if (result.getId() == 0) {
            throw new SaveDataException(Messages.get("error.exch.not.saved",
                    exchange.getBaseCurrency().getCode(),
                    exchange.getQuoteCurrency().getCode()));
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
     * @return Saved exchange.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    public Exchange save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          BigDecimal rate, @NotNull Date valueDate) throws SaveDataException {
        return save(new Exchange(ccy1, ccy2, rate, valueDate));
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
     * @return Saved exchange.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    public Exchange save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          Double rate,  @NotNull Date valueDate) throws SaveDataException {
        return save(ccy1, ccy2, BigDecimal.valueOf(rate), valueDate);
    }

    /**
     * Save all {@link Exchange} objects in a list.
     *
     * @param exchanges List of exchanges to save.
     *
     * @return List of saved exchange.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override
    @Transactional
    public List<Exchange> saveAll(Collection<Exchange> exchanges) throws SaveDataException {
        List<Exchange> saved = new ArrayList<>();
        List<Exchange> notSaved = new ArrayList<>();

        for (Exchange exchange : exchanges) {
            try {
                Exchange savedExch = save(exchange);
                saved.add(savedExch);
            } catch (SaveDataException e) {
                notSaved.add(exchange);
            }
        }

        if (!notSaved.isEmpty()) {
            throw new SaveDataException(
                    Messages.get("error.some.exch.not.saved", notSaved.toString()));
        }

        return saved;
    }

    private void handleCurrency(Currency currency) {
        if (currency.getId() == 0) {
            currency = serviceFactory.create(CurrencyService.class)
                    .find(currency.getCode());
        }
    }
}
