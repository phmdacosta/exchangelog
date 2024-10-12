package com.pedrocosta.exchangelog.currency;

import com.pedrocosta.exchangelog.RepositoryService;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.sun.istack.Nullable;

import java.util.Collection;
import java.util.List;

public interface CurrencyService extends RepositoryService<Currency> {

    /**
     * Find a specific currency from database.
     *
     * @param code Currency code
     *
     * @return  Found currency. Null if not found.
     */
    @Nullable Currency find(String code);

    /**
     * Find a specific currency from database.
     *
     * @param id    Currency id in database
     *
     * @return Found currency. Null if not found.
     */
    @Override
    @Nullable Currency find(long id);

    /**
     * Get all currencies from database.
     *
     * @return List of found currencies. Empty list if not found.
     */
    @Override List<Currency> findAll();

    /**
     * Save a currency into database.
     *
     * @param currency Currency to save
     *
     * @return Saved currency.
     *         If error, thorws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override Currency save(Currency currency) throws SaveDataException;

    /**
     * Save all currencies from a list into database.
     *
     * @param currencies List of currencies to save
     *
     * @return List of saved currencies.
     *         If error, thorws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override List<Currency> saveAll(Collection<Currency> currencies) throws SaveDataException;
}
