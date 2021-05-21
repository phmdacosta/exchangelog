package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.CurrencyRepository;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
import com.sun.istack.Nullable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class CurrencyService implements RepositoryService<Currency> {

    private CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * Find a specific currency from database.
     *
     * @param code Currency code
     *
     * @return {@link Currency} object.
     */
    @Nullable
    public Currency find(String code) {
        return repository.findByCode(code);
    }

    @Override
    @Nullable
    public Currency find(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Get all currencies from database.
     *
     * @return List of {@link Currency}.
     */
    @Override
    public List<Currency> findAll() {
        return repository.findAll();
    }

    /**
     * Save a currency into database.
     *
     * @param currency Currency to save
     *
     * @return Saved currency.
     */
    @Override
    public Currency save(Currency currency) {
        return repository.save(currency);
    }

    /**
     * Save all currencies from a list into database.
     *
     * @param currencies List of currencies to save
     *
     * @return List of saved currencies.
     */
    @Override
    public List<Currency> saveAll(Collection<Currency> currencies) {
        return repository.saveAll(currencies);
    }
}
