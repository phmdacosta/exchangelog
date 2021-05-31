package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.persistence.CurrencyRepository;
import com.pedrocosta.exchangelog.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CurrencyService implements RepositoryService<Currency> {

    private final CurrencyRepository repository;

    public CurrencyService(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * Find a specific currency from database.
     *
     * @param code Currency code
     *
     * @return  {@link ServiceResponse} object with found currency.
     *          If currency not found, returns {@link ServiceResponse} with error message.
     */
    public ServiceResponse<Currency> find(String code) {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.OK);
        result.setObject(repository.findByCode(code));

        if (result.getObject() == null) {
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage(Messages.get("error.ccy.not.found", code));
        }

        return result;
    }

    /**
     * Find a specific currency from database.
     *
     * @param id    Currency id in database
     *
     * @return  {@link ServiceResponse} object with found currency.
     *          If currency not found, returns {@link ServiceResponse} with error message.
     */
    @Override
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
     * @return {@link ServiceResponse} object with list of found currencies.
     *         If currency not found, returns {@link ServiceResponse} with error message.
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
     * @return {@link ServiceResponse} object with saved currency.
     *         If error, returns {@link ServiceResponse} with error message.
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
     * @return {@link ServiceResponse} object with list of saved currencies.
     *         If error, returns {@link ServiceResponse} with error message.
     */
    @Override
    public ServiceResponse<List<Currency>> saveAll(Collection<Currency> currencies) {
        ServiceResponse<List<Currency>> result = new ServiceResponse<>(HttpStatus.CREATED);
        result.setObject(repository.saveAll(currencies));

        if (result.getObject() == null || result.getObject().isEmpty()) {
            result = new ServiceResponse<>(HttpStatus.BAD_REQUEST);
            result.setMessage(Messages.get("error.not.saved", "any currency"));
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
