package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.utils.DatabaseOrder;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface ExchangeService extends RepositoryService<Exchange> {

    /**
     * Find a exchanges based on it ID.
     *
     * @param id ID of exchange
     * @return  Found exchange. Null if not found.
     */
    @Override
    @Nullable Exchange find(long id);

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable List<Exchange> find(@NotNull Currency ccy, Date valueDate);

    /**
     * Find a specific exchange based on parameters in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param valueDate Valuation date of rate
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate);

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
    @Nullable Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate, double amount);

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
    @Nullable Exchange find(@NotNull Currency ccy1, @NotNull Currency ccy2, Date valueDate, BigDecimal amount);

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2);

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param amount    Amount of ccy1.
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2, double amount);

    /**
     * Find the last exchange in database.
     *
     * @param ccy1      Base currency of exchange
     * @param ccy2      Quote currency of exchange
     * @param amount    Amount of ccy1.
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange findLast(@NotNull Currency ccy1, @NotNull Currency ccy2, BigDecimal amount);

    /**
     * Find exchange with minimum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange findWithMinRate(Date startValueDate, Date endValueDate);

    /**
     * Find exchange with maximum rate in a period from database.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  Found exchange. Null if not found.
     */
    @Nullable Exchange findWithMaxRate(Date startValueDate, Date endValueDate);

    /**
     * List all exchanges from database.
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    @Override List<Exchange> findAll();

    /**
     * List all exchanges from database between a period of time.
     *
     * @param base  Base currency of exchange
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAll(Currency base);

    /**
     * List all exchanges from database between a period of time.
     *
     * @param base      Base currency of exchange
     * @param amount    Amount of base currency
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAll(Currency base, double amount);

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAll(@NotNull Currency ccy, Date valueDate, double amount);

    /**
     * Find exchanges based on parameters in database.
     *
     * @param ccy       Base currency of exchange
     * @param valueDate Valuation date of rate
     * @param amount    Amount of base currency
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAll(@NotNull Currency ccy, Date valueDate, BigDecimal amount);

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return  List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAll(Date startValueDate, Date endValueDate);

    /**
     * List all exchanges from database between a period of time.
     *
     * @param startValueDate    Start date
     * @param endValueDate      End date
     *
     * @return List of found exchanges. Empty list if not found.
     */
    List<Exchange> findAllOrderByValueDate(Date startValueDate, Date endValueDate);

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
    List<Exchange> findAllOrderedByRate(Date startValueDate, Date endValueDate, DatabaseOrder order);

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
    @Override Exchange save(Exchange exchange) throws SaveDataException;

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
    Exchange save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          BigDecimal rate, @NotNull Date valueDate) throws SaveDataException;

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
    Exchange save(@NotNull Currency ccy1, @NotNull Currency ccy2,
                                          Double rate,  @NotNull Date valueDate) throws SaveDataException;

    /**
     * Save all {@link Exchange} objects in a list.
     *
     * @param exchanges List of exchanges to save.
     *
     * @return List of saved exchange.
     *         If error, throws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override List<Exchange> saveAll(Collection<Exchange> exchanges) throws SaveDataException;
}
