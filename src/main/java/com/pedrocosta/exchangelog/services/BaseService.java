package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import org.codehaus.jettison.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Super service for common functions.
 *
 * @author Pedro H M da Costa
 */
@Service
public abstract class BaseService implements BusinessService {

    protected final ServiceFactory factory;
    protected final Environment env;
    protected final Messages messages;

    public BaseService(ServiceFactory factory, Environment env,
                       Messages messages) {
        this.factory = factory;
        this.env = env;
        this.messages = messages;
    }

    protected String getProjectEngine() {
        return this.env.getProperty(PropertyNames.PROJECT_ENGINE);
    }

    @Override
    public List<Currency> loadCurrencies() throws JSONException, NoSuchDataException {
        // First get currencies from database
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);
//        ServiceResponse<List<Currency>> result = ccyService.findAll();
        List<Currency> result = ccyService.findAll();

        if (result.isEmpty()) {
            // If no currency found in database, retrieve currencies from API
            try {
                BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
                List<Currency> apiCurrencies = apiService.loadCurrencies();
                result = ccyService.saveAll(apiCurrencies);
            } catch (NoSuchDataException | SaveDataException e) {
                throw new NoSuchDataException(e);
            }
        }

//        try {
//            result = ccyService.findAll();
//        } catch (NoSuchDataException noSuchDataEx) {
//            // If no currency found in database, retrieve currencies from API
//            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
//            List<Currency> apiCurrencies = apiService.loadCurrencies();
//            try {
//                result = ccyService.saveAll(apiCurrencies);
//            } catch (SaveDataException saveException) {
//                throw new NoSuchDataException(saveException);
//            }
//        }

//        if (!result.isEmpty()) {
//            // If no currency found in database, retrieve currencies from API
//            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
////            ServiceResponse<List<Currency>> apiResponse = apiService.loadCurrencies();
//            List<Currency> apiCurrencies = apiService.loadCurrencies();
//
//            if (apiCurrencies.isEmpty()) {
//                throw new ServiceException(messages.getMessage("error.no.ccy.found"));
//            }
//
//            result = ccyService.saveAll(apiCurrencies);
//
////            if (!apiCurrencies.isEmpty()) { // Saving currencies got by API
////                result = ccyService.saveAll(apiCurrencies);
////
//////                if (!respSave.isSuccess()) {
//////                    // Could not save, return generic server error
//////                    throw new ServiceException(messages.getMessage("error.no.ccy.found"));
////////                    return ServiceResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
////////                            messages.getMessage("error.no.ccy.found"));
//////                }
//////                result = ServiceResponse.<List<Currency>>createSuccess()
//////                        .setObject(respSave.getObject());
////
////            } else { // If no currency found in API, return error
////
//////                result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//////                        messages.getMessage("error.no.ccy.found"));
////            }
//        }

        return result;
    }

    public Currency loadCurrency(String code) throws JSONException, NoSuchDataException {
        // First get currency from database
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);
        Currency result = ccyService.find(code);

        if (result == null) {
            // If no currency found in database, retrieve it from API
            try {
                Currency apiCurrency = ((BusinessService) factory.create(getProjectEngine()))
                        .loadCurrency(code);
                result = ccyService.save(apiCurrency);
            } catch (NoSuchDataException | SaveDataException e) {
                throw new NoSuchDataException(e);
            }
        }

//        try {
//            result = ccyService.find(code);
//        } catch (NoSuchDataException e) {
//            // If no currency found in database, retrieve it from API
//            Currency apiCurrency = ((BusinessService) factory.create(getProjectEngine()))
//                    .loadCurrency(code);
//            try {
//                result = ccyService.save(apiCurrency);
//            } catch (SaveDataException saveDataException) {
//                throw new NoSuchDataException(saveDataException);
//            }
//        }

//        if (result == null) {
//            // If no currency found in database, retrieve it from API
//            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
//            ServiceResponse<Currency> apiResponse = apiService.loadCurrency(code);
//
//            if (apiResponse.isSuccess()) { // Saving currency got by API
//                ServiceResponse<Currency> respSave = ccyService.save(apiResponse.getObject());
//                if (!respSave.isSuccess()) {
//                    // Could not save, return generic server error
//                    return ServiceResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR,
//                            messages.getMessage("error.ccy.not.found", code));
//                }
//                result = ServiceResponse.<Currency>createSuccess()
//                        .setObject(respSave.getObject());
//
//            } else { // If no currency found in API, return error
//                result = ServiceResponse.createError(HttpStatus.NOT_FOUND,
//                        messages.getMessage("error.ccy.not.found", code));
//            }
//        }

        return result;
    }
}
