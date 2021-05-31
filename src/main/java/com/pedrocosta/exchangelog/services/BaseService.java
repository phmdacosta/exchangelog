package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.utils.Messages;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import org.codehaus.jettison.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
    public ServiceResponse<List<Currency>> loadCurrencies() throws JSONException {
        // First get currencies from database
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);
        ServiceResponse<List<Currency>> result = ccyService.findAll();

        if (!result.isSuccess()) {
            // If no currency found in database, retrieve currencies from API
            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
            ServiceResponse<List<Currency>> response = apiService.loadCurrencies();

            if (response.isSuccess()) { // Saving currencies got by API
                ServiceResponse<List<Currency>> respSave = ccyService.saveAll(response.getObject());
                if (!respSave.isSuccess()) {
                    // Could not save, return generic server error
                    result = new ServiceResponse<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    result.setMessage(messages.getMessage("error.no.ccy.found"));
                }
                result = new ServiceResponse<>(HttpStatus.OK);
                result.setObject(respSave.getObject());

            } else { // If no currency found in API, return error
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage(messages.getMessage("error.no.ccy.found"));
            }
        }

        return result;
    }

    public ServiceResponse<Currency> loadCurrency(String code) throws JSONException {
        // First get currency from database
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);
        ServiceResponse<Currency> result = ccyService.find(code);

        if (!result.isSuccess()) {
            // If no currency found in database, retrieve it from API
            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
            ServiceResponse<Currency> response = apiService.loadCurrency(code);

            if (response.isSuccess()) { // Saving currency got by API
                ServiceResponse<Currency> respSave = ccyService.save(response.getObject());
                if (!respSave.isSuccess()) {
                    // Could not save, return generic server error
                    result = new ServiceResponse<>(HttpStatus.INTERNAL_SERVER_ERROR);
                    result.setMessage(messages.getMessage("error.ccy.not.found", code));
                }
                result = new ServiceResponse<>(HttpStatus.OK);
                result.setObject(respSave.getObject());

            } else { // If no currency found in API, return error
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage(messages.getMessage("error.ccy.not.found", code));
            }
        }

        return result;
    }
}
