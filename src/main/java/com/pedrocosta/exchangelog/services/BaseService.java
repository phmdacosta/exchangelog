package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.persistence.CurrencyRepository;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
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

    protected ServiceFactory factory;
    protected Environment env;
//    @Autowired
//    protected FixerService fixerService;
    @Autowired
    protected CurrencyRepository currencyRepo;

    public BaseService(ServiceFactory factory, Environment env) {
        this.factory = factory;
        this.env = env;
    }

    protected String getProjectEngine() {
        return this.env.getProperty(PropertyNames.PROJECT_ENGINE);
    }

    @Override
    public ServiceResponse<List<Currency>> loadCurrencies() throws JSONException {
        ServiceResponse<List<Currency>> result = new ServiceResponse<>(HttpStatus.OK);

        BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);

        List<Currency> currencies = ccyService.findAll();

        if (currencies == null || currencies.isEmpty()) {
            ServiceResponse<List<Currency>> response = apiService.loadCurrencies();

            if (response.isSuccess()) {
                currencies = ccyService.saveAll(response.getObject());
            } else {
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage("Could not find any currency");
            }
        }

        result.setObject(currencies);

        return result;
    }

    public ServiceResponse<Currency> loadCurrency(String code) throws JSONException {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.OK);

        BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);

        Currency ccy = ccyService.find(code);

        if (ccy == null) {
            ServiceResponse<Currency> response = apiService.loadCurrency(code);

            if (response.isSuccess()) {
                ccy = ccyService.save(response.getObject());
            } else {
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage("Could not find currency " + code);
            }
        }

        result.setObject(ccy);
        return result;
    }
}
