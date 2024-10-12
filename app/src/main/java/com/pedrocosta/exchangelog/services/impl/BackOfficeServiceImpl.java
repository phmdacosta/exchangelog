package com.pedrocosta.exchangelog.services.impl;

import com.pedrocosta.exchangelog.ServiceFactory;
import com.pedrocosta.exchangelog.currency.Currency;
import com.pedrocosta.exchangelog.currency.CurrencyService;
import com.pedrocosta.exchangelog.exceptions.NoSuchDataException;
import com.pedrocosta.exchangelog.exceptions.SaveDataException;
import com.pedrocosta.exchangelog.exchange.Exchange;
import com.pedrocosta.exchangelog.exchange.ExchangeService;
import com.pedrocosta.exchangelog.services.BackOfficeService;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.exchangelog.utils.Defaults;
import com.pedrocosta.exchangelog.utils.PropertyNames;
import com.pedrocosta.springutils.output.Log;
import org.codehaus.jettison.json.JSONException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service to execute business for batch processes.
 *
 * @author Pedro H M da Costa
 */
@Service
public class BackOfficeServiceImpl implements BackOfficeService {

    protected final ServiceFactory factory;
    protected final Environment env;

    public BackOfficeServiceImpl(ServiceFactory factory, Environment env) {
        this.factory = factory;
        this.env = env;
    }

    protected String getProjectEngine() {
        return this.env.getProperty(PropertyNames.PROJECT_ENGINE);
    }

    @Override
    public List<Currency> loadCurrencies() throws JSONException, NoSuchDataException {
        // First get currencies from database
        CurrencyService ccyService = (CurrencyService) factory.create(Currency.class);
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

        return result;
    }

    /**
     * Calculate all remain quote rates base on existing rates.
     *
     * @param exchanges List of existing exchanges
     *
     * @return List of old exchanges with new ones in addition.
     */
    public List<Exchange> calculateOthersQuotes(List<Exchange> exchanges) {
        List<Exchange> result = null;

        if (exchanges != null) {
            result = new ArrayList<>(exchanges);

            for (int i = 0; i < exchanges.size(); i++) {
                Exchange exchange1 = exchanges.get(i);
                Currency baseCcy = exchange1.getQuoteCurrency();
                BigDecimal baseRate = exchange1.getRate();

                for (int j = 1; j < exchanges.size(); j++) {
                    Exchange exchange2 = exchanges.get(j);
                    Currency quoteCcy = exchange2.getQuoteCurrency();
                    BigDecimal quoteRate = exchange2.getRate();

                    if (exchange1.getBaseCurrency().equals(quoteCcy)
                            || exchange1.getQuoteCurrency().equals(quoteCcy)) {
                        continue;
                    }

                    try {
                        BigDecimal newRate = quoteRate.divide(baseRate, Defaults.ROUNDING_MODE);

                        Exchange newExch = new Exchange(baseCcy, quoteCcy,
                                newRate, exchange2.getValueDate());

                        Exchange dbExch = ((ExchangeService) factory.create(Exchange.class))
                                .find(newExch.getBaseCurrency(), newExch.getQuoteCurrency(),
                                        newExch.getValueDate());

                        if (dbExch != null) {
                            newExch.setId(dbExch.getId());
                        }

                        result.add(newExch);
                    } catch (ArithmeticException e) {
                        Log.error(this, e);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Not supported.
     */
    @Deprecated
    @Override
    public List<Exchange> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) {
        return null;
    }
}
