package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
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
public class ExchangeService implements RepositoryService<Exchange> {

    private final ExchangeRepository repository;
    private final ServiceFactory serviceFactory;

    public ExchangeService(ExchangeRepository repository, ServiceFactory serviceFactory) {
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
//        ServiceResponse<Exchange> result = ServiceResponse.<Exchange>createSuccess()
//                .setObject(repository.findById(id).orElse(null));
//        Exchange result = repository.findById(id).orElse(null);

//        if (result == null) {
//            String arg = "with id " + id;
//            throw new NoSuchDataException(Messages.get("error.exch.not.found", arg));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found", arg));
//        }

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
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess()
//                        .setObject(repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate));
//        List<Exchange> result = repository.findAllByBaseCurrencyAndValueDate(ccy, valueDate);

//        if (result == null || result.isEmpty()) {
//            String arg = "of " + ccy.getCode() + " in " + valueDate;
//            throw new NoSuchDataException(Messages.get("error.exch.not.found", arg));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found", arg));
//        }

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
//        ServiceResponse<Exchange> result = ServiceResponse.<Exchange>createSuccess()
//                .setObject(repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
//                    ccy1, ccy2, valueDate));
//        Exchange result = repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
//                ccy1, ccy2, valueDate);

//        if (result == null) {
//            String arg = ccy1.getCode() + "/" + ccy2.getCode() + " in " + valueDate;
//            throw new NoSuchDataException(Messages.get("error.exch.not.found", arg));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found", arg));
//        }

        return repository.findByBaseCurrencyAndQuoteCurrencyAndValueDate(
                ccy1, ccy2, valueDate);
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
//        ServiceResponse<Exchange> result = ServiceResponse.<Exchange>createSuccess()
//                .setObject(repository.findFirstByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
//                    ccy1, ccy2));
//        Exchange result = repository.findFirstByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
//                ccy1, ccy2);

//        if (result == null) {
//            String arg = ccy1.getCode() + "/" + ccy2.getCode();
//            throw new NoSuchDataException(Messages.get("error.exch.not.found", arg));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found", arg));
//        }

        return repository.findFirstByBaseCurrencyAndQuoteCurrencyOrderByValueDateDesc(
                ccy1, ccy2);
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
//        ServiceResponse<Exchange> result = ServiceResponse.<Exchange>createSuccess()
//                .setObject(repository.findByValueDateBetweenOrderByRateAsc(
//                        startValueDate, endValueDate));

//        Exchange result = repository.findByValueDateBetweenOrderByRateAsc(
//                startValueDate, endValueDate);

//        if (result == null) {
//            throw new NoSuchDataException(Messages.get("error.exch.not.found.in.dates",
//                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found.in.dates",
////                        String.valueOf(startValueDate), String.valueOf(endValueDate)));
//        }

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
//        ServiceResponse<Exchange> result = ServiceResponse.<Exchange>createSuccess()
//                .setObject(repository.findByValueDateBetweenOrderByRateDesc(
//                        startValueDate, endValueDate));

//        if (result.getObject() == null) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.exch.not.found.in.dates",
//                        String.valueOf(startValueDate), String.valueOf(endValueDate)));
//        }

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
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess()
//                        .setObject(repository.findAll());
//        List<Exchange> result = repository.findAll();

//        if (result.isEmpty()) {
//            throw new NoSuchDataException(Messages.get("error.no.exch.found"));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.no.exch.found"));
//        }

        return repository.findAll();
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
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess()
//                        .setObject(repository.findAllByValueDateBetween(
//                                startValueDate, endValueDate));
//        List<Exchange> result =repository.findAllByValueDateBetween(
//                startValueDate, endValueDate);

//        if (result == null || result.isEmpty()) {
//            throw new NoSuchDataException(Messages.get("error.exch.not.found.in.dates",
//                    String.valueOf(startValueDate), String.valueOf(endValueDate)));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.exch.not.found.in.dates",
////                        String.valueOf(startValueDate), String.valueOf(endValueDate)));
//        }

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
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess()
//                        .setObject(repository.findAllByValueDateBetweenOrderByValueDateDesc(
//                                startValueDate, endValueDate));
//
//        if (result.getObject() == null) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.exch.not.found.in.dates",
//                        String.valueOf(startValueDate), String.valueOf(endValueDate)));
//        }

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
//        ServiceResponse<List<Exchange>> result = ServiceResponse.createSuccess();
        List<Exchange> result = new ArrayList<>();

        if (DatabaseOrder.DESC == order) {
            result = repository.findAllByValueDateBetweenOrderByRateDesc(
                    startValueDate, endValueDate);
        } else {
            result = repository.findAllByValueDateBetweenOrderByRateAsc(
                    startValueDate, endValueDate);
        }

//        if (result.getObject() == null) {
//            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                    Messages.get("error.exch.not.found.in.dates",
//                        String.valueOf(startValueDate), String.valueOf(endValueDate)));
//        }

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
        // Check currencies
//        if (exchange.getBaseCurrency() == null || exchange.getQuoteCurrency() == null) {
//            throw new ServiceException(Messages.get("error.exch.save.wrong.ccy"));
////            return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
////                    Messages.get("error.exch.save.wrong.ccy"));
//        }

        try {
            CurrencyService ccyServ = (CurrencyService) serviceFactory.create(Currency.class);
            if (exchange.getBaseCurrency().getId() == 0) {
                Currency baseCcy = ccyServ.find(exchange.getBaseCurrency().getCode());
                if (baseCcy == null) {
                    throw new NoSuchDataException(
                            Messages.get("error.ccy.not.found",
                                    exchange.getBaseCurrency().getCode()));
                }
//            ServiceResponse<Currency> baseCcyResp = ccyServ.find(exchange
//                    .getBaseCurrency().getCode());
//                if (!baseCcyResp.isSuccess()) {
//                    return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                            Messages.get("error.exch.save.wrong.ccy"));
//                }
                exchange.getBaseCurrency().setId(baseCcy.getId());
            }
            if (exchange.getQuoteCurrency().getId() == 0) {
                Currency quoteCcy = ccyServ.find(exchange.getQuoteCurrency().getCode());
                if (quoteCcy == null) {
                    throw new NoSuchDataException(
                            Messages.get("error.ccy.not.found",
                                    exchange.getQuoteCurrency().getCode()));
                }
//                ServiceResponse<Currency> quoteCcyResp = ccyServ.find(exchange
//                        .getQuoteCurrency().getCode());
//                if (!quoteCcyResp.isSuccess()) {
//                    return ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                            Messages.get("error.exch.save.wrong.ccy"));
//                }
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
//            ServiceResponse<Exchange> lastExchResp = find(exchange.getBaseCurrency(),
//                    exchange.getQuoteCurrency(), exchange.getValueDate());
            // If exchange exists, we get its id to update
//            if (lastExchResp.isSuccess()) {
//                exchange.setId(lastExchResp.getObject().getId());
//            }
        }

        // Execute save
        Exchange result = repository.save(exchange);
//        ServiceResponse<Exchange> result =
//                ServiceResponse.<Exchange>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.save(exchange));
        if (result.getId() == 0) {
            throw new SaveDataException(Messages.get("error.exch.not.saved",
                    exchange.getBaseCurrency().getCode(),
                    exchange.getQuoteCurrency().getCode()));
        }

//        if (result.getObject() == null || result.getObject().getId() == 0) {
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.exch.not.saved",
//                        exchange.getBaseCurrency().getCode(),
//                            exchange.getQuoteCurrency().getCode()));
//        }

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
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess(HttpStatus.CREATED);
        List<Exchange> saved = new ArrayList<>();
        List<Exchange> notSaved = new ArrayList<>();

        for (Exchange exchange : exchanges) {
            try {
                Exchange savedExch = save(exchange);
                saved.add(savedExch);
            } catch (SaveDataException e) {
                notSaved.add(exchange);
            }
//            ServiceResponse<Exchange> resultSingleSave = save(exchange);

//            if (!resultSingleSave.isSuccess()) {
//                notSaved.add(exchange);
//            } else {
//                saved.add(resultSingleSave.getObject());
//            }
        }

        if (!notSaved.isEmpty()) {
            throw new SaveDataException(
                    Messages.get("error.some.exch.not.saved", notSaved.toString()));
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.some.exch.not.saved", notSaved.toString()));
        }

        return saved;
    }
//    public ServiceResponse<List<Exchange>> saveAll(Collection<Exchange> exchanges) {
//        ServiceResponse<List<Exchange>> result =
//                ServiceResponse.<List<Exchange>>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.saveAll(exchanges));
//
//        if (result.getObject() == null || result.getObject().isEmpty()) {
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.not.saved", "any exchange"));
//        }
//        else if(result.getObject().size() != exchanges.size()) {
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST);
//            List<Exchange> notSaved = new ArrayList<>();
//            ServiceResponse<List<Exchange>> auxResult = result;
//            exchanges.forEach(exchange -> {
//                if (!auxResult.getObject().contains(exchange)) {
//                    notSaved.add(exchange);
//                }
//            });
//            String msg = Messages.get("error.some.exch.not.saved", notSaved.toString());
//            result.setMessage(msg);
//        }
//
//        return result;
//    }
}
