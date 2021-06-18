package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.persistence.CurrencyRepository;
import com.pedrocosta.exchangelog.utils.Messages;
import com.sun.istack.Nullable;
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
     * @return  Found currency. Null if not found.
     */
    @Nullable
    public Currency find(String code) {
//        ServiceResponse<Currency> result = ServiceResponse.<Currency>createSuccess()
//                .setObject(repository.findByCode(code));
//        Currency result = repository.findByCode(code);

//        if (result == null) {
//            throw new NoSuchDataException(Messages.get("error.ccy.not.found", code));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.ccy.not.found", code));
//        }

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
//        ServiceResponse<Currency> result = ServiceResponse.<Currency>createSuccess()
//                .setObject(repository.findById(id).orElse(null));

//        Currency result = repository.findById(id).orElse(null);

//        if (result == null) {
//            String arg = "with id " + id;
//            throw new NoSuchDataException(Messages.get("error.ccy.not.found", arg));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.ccy.not.found", arg));
//        }

        return repository.findById(id).orElse(null);
    }

    /**
     * Get all currencies from database.
     *
     * @return List of found currencies. Empty list if not found.
     */
    @Override
    public List<Currency> findAll() {
//        ServiceResponse<List<Currency>> result =
//                ServiceResponse.<List<Currency>>createSuccess()
//                        .setObject(repository.findAll());

//        List<Currency> result = repository.findAll();

//        if (result.isEmpty()) {
//            throw new NoSuchDataException(Messages.get("error.no.ccy.found"));
////            result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
////                    Messages.get("error.no.ccy.found"));
//        }

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
//        ServiceResponse<Currency> result =
//                ServiceResponse.<Currency>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.save(currency));

        Currency result = repository.save(currency);

        if (result.getId() == 0) {
            String arg = "currency ".concat(currency.getCode());
            throw new SaveDataException(Messages.get("error.not.saved", arg));
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.not.saved", arg));
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
//        ServiceResponse<List<Currency>> result =
//                ServiceResponse.<List<Currency>>createSuccess(HttpStatus.CREATED)
//                        .setObject(repository.saveAll(currencies));
        List<Currency> result = repository.saveAll(currencies);

        if (result.isEmpty()) {
            throw new SaveDataException(Messages.get("error.not.saved", "any currency"));
//            result = ServiceResponse.createError(HttpStatus.BAD_REQUEST,
//                    Messages.get("error.not.saved", "any currency"));
        }
        else if(result.size() != currencies.size()) {
            List<Currency> notSaved = new ArrayList<>();
//            ServiceResponse<List<Currency>> auxResult = result.setCode(HttpStatus.BAD_REQUEST);
            currencies.forEach(currency -> {
                if (!result.contains(currency)) {
                    notSaved.add(currency);
                }
            });
//            String msg = Messages.get("error.some.ccy.not.saved", notSaved.toString());
            throw new SaveDataException(
                    Messages.get("error.some.ccy.not.saved", notSaved.toString()));
//            result.setMessage(msg);
        }

        return result;
    }
}
