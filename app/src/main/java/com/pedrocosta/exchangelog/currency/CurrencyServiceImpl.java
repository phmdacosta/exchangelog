package com.pedrocosta.exchangelog.currency;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.springutils.output.Messages;
import com.sun.istack.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository repository;

    public CurrencyServiceImpl(CurrencyRepository repository) {
        this.repository = repository;
    }

    /**
     * Find a specific currency from database.
     *
     * @param code Currency code
     *
     * @return  Found currency. Null if not found.
     */
    @Nullable
    public Currency find(String code) {
        return repository.findByCode(code);
    }

    /**
     * Find a specific currency from database.
     *
     * @param id    Currency id in database
     *
     * @return Found currency. Null if not found.
     */
    @Override
    @Nullable
    public Currency find(long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Get all currencies from database.
     *
     * @return List of found currencies. Empty list if not found.
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
     *         If error, thorws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override
    public Currency save(Currency currency) throws SaveDataException {
        Currency result = repository.save(currency);

        if (result.getId() == 0) {
            String arg = "currency ".concat(currency.getCode());
            throw new SaveDataException(Messages.get("error.not.saved", arg));
        }

        return result;
    }

    /**
     * Save all currencies from a list into database.
     *
     * @param currencies List of currencies to save
     *
     * @return List of saved currencies.
     *         If error, thorws {@link SaveDataException} with error message.
     * @throws SaveDataException if error
     */
    @Override
    public List<Currency> saveAll(Collection<Currency> currencies) throws SaveDataException {
        List<Currency> result = repository.saveAll(currencies);

        if (result.isEmpty()) {
            throw new SaveDataException(Messages.get("error.not.saved", "any currency"));
        }
        else if(result.size() != currencies.size()) {
            List<Currency> notSaved = new ArrayList<>();
            currencies.forEach(currency -> {
                if (!result.contains(currency)) {
                    notSaved.add(currency);
                }
            });
            throw new SaveDataException(
                    Messages.get("error.some.ccy.not.saved", notSaved.toString()));
        }

        return result;
    }
}
