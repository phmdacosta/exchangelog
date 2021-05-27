package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.persistence.CurrencyRepository;
import com.pedrocosta.exchangelog.persistence.ExchangeRepository;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.Nullable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ServiceResponse<Currency> find(String code) {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByCode(code));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.ccy.not.found", code));
        }

        return result;
    }

    @Override
    @Nullable
    public ServiceResponse<Currency> find(long id) {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findById(id).orElse(null));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            String arg = "with id " + id;
            result.setMessage(Messages.get("error.ccy.not.found", arg));
        }

        return result;
    }

    /**
     * Get all currencies from database.
     *
     * @return List of {@link Currency}.
     */
    @Override
    public ServiceResponse<List<Currency>> findAll() {
        ServiceResponse<List<Currency>> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findAll());

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.no.ccy.found"));
        }

        return result;
    }

    /**
     * Save a currency into database.
     *
     * @param currency Currency to save
     *
     * @return Saved currency.
     */
    @Override
    public ServiceResponse<Currency> save(Currency currency) {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.save(currency));

        if (result.getObject() == null || result.getObject().getId() == 0) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            String arg = "currency ".concat(currency.getCode());
            result.setMessage(Messages.get("error.not.saved", arg));
        }

        return result;
    }

    /**
     * Save all currencies from a list into database.
     *
     * @param currencies List of currencies to save
     *
     * @return List of saved currencies.
     */
    @Override
    public ServiceResponse<List<Currency>> saveAll(Collection<Currency> currencies) {
        ServiceResponse<List<Currency>> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.saveAll(currencies));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            String arg = "any currency";
            result.setMessage(Messages.get("error.not.saved", arg));
        }
        else if(result.getObject().size() != currencies.size()) {
            List<Currency> notSaved = new ArrayList<>();
            ServiceResponse<List<Currency>> auxResult = result.setCode(HttpStatus.BAD_REQUEST);
            currencies.forEach(currency -> {
                if (!auxResult.getObject().contains(currency)) {
                    notSaved.add(currency);
                }
            });
            String msg = Messages.get("error.some.ccy.not.saved", notSaved.toString());
            result.setMessage(msg);
        }

        return result;
    }
}
