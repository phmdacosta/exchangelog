package com.pedrocosta.exchangelog.services;

import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.utils.Defaults;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.Messages;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Service to execute business for batch processes.
 *
 * @author Pedro H M da Costa
 */
@Service
public class BackOfficeService extends BaseService {


    public BackOfficeService(ServiceFactory factory, Environment env,
                             Messages messages) {
        super(factory, env, messages);
    }

    /**
     * Update database with quote rates based um a currency.
     *
     * @param baseCcyCode Base currency.
     *
     * @return {@link ServiceResponse} of a list of {@link Exchange} with new rates.
     */
    public ServiceResponse<List<Exchange>> updateQuoteValues(String baseCcyCode) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse(HttpStatus.OK);

        try {
            BusinessService apiService = (BusinessService) factory.create(getProjectEngine());
            ServiceResponse<List<Exchange>> response =
                    apiService.getQuoteRate(baseCcyCode, null, 1D, new Date());
            List<Exchange> quotes = new ArrayList<>();

            if (response.isSuccess()) {
                ServiceResponse<Currency> responseCcy = loadCurrency(baseCcyCode);

                if (!responseCcy.isSuccess()) {
                    return result.fromError(responseCcy);
                }

                Currency base = responseCcy.getObject().clone();

                List<Exchange> rates = response.getObject();
                Iterator<Exchange> itRates = rates.iterator();

                ExchangeService exchService =
                        (ExchangeService) factory.create(ExchangeService.class);

                while (itRates.hasNext()) {
                    Exchange exchange = itRates.next();
                    String quoteCode = exchange.getQuoteCurrency().getCode();
                    if (quoteCode.equals(baseCcyCode)) {
                        continue;
                    }
                    responseCcy = loadCurrency(quoteCode);

                    if (!responseCcy.isSuccess()) {
                        return result.fromError(responseCcy);
                    }

                    Currency quote = loadCurrency(quoteCode).getObject();
                    Date valDate = exchange.getValueDate();

                    // Exchange rate for base currency
//                    double baseRate = exchange.getRate();
                    BigDecimal baseRate = exchange.getRate();
                    quotes.add(exchService.save(base, quote, baseRate, valDate));

                    // Exchange rate for quote currency
//                    double quoteRate = 1 / baseRate;
                    BigDecimal quoteRate = BigDecimal.ONE.divide(baseRate, Defaults.ROUNDING_MODE);
                    exchService.save(quote, base, quoteRate, valDate);
                }

                quotes = calculateOthersQuotes(quotes);
                result.setObject(quotes);
            } else {
                result = response; // Return error
            }
        } catch (JSONException | CloneNotSupportedException e) {
            result = new ServiceResponse(HttpStatus.BAD_REQUEST);
            result.setMessage(messages.getMessage("could.not.update",
                    "quote values"));
        }

        return result;
    }

    /**
     * Update a specific quote rate.
     *
     * @param ccy1      Base currency
     * @param ccy2      Quote currency
     * @param rate      Rate value
     * @param valueDate Valuation date of the rate
     *
     * @return {@link ServiceResponse} with new {@link Exchange}.
     */
    public ServiceResponse<Exchange> updateQuote(Currency ccy1, Currency ccy2, BigDecimal rate, Date valueDate) {
        ServiceResponse<Exchange> result = new ServiceResponse(HttpStatus.OK);

        ExchangeService exchService =
                (ExchangeService) factory.create(ExchangeService.class);

        Exchange exchange = exchService.save(ccy1, ccy2, rate, valueDate);
        if (exchange.getId() <= 0) {
            result = new ServiceResponse(HttpStatus.BAD_REQUEST);
            String arg = "exchange".concat(ccy1.getCode()).concat("/").concat(ccy2.getCode());
            result.setMessage(messages.getMessage("could.not.update",
                    arg));
        }

        return result;
    }

    @Override
    public ServiceResponse<Currency> loadCurrency(String code) throws JSONException {
        return super.loadCurrency(code);
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

//                    double newRate = CustomMath.division(baseRate, quoteRate);
                    try {
                        BigDecimal newRate = quoteRate.divide(baseRate, Defaults.ROUNDING_MODE);

                        result.add(new Exchange(baseCcy, quoteCcy,
                                newRate, exchange2.getValueDate()));
                    } catch (ArithmeticException e) {
                        Log.error(this, e);
                    }
                }
            }
        }

        return result;
    }

    @Deprecated
    private ServiceResponse getResponseErrorFromJson(JSONObject json) throws JSONException {
        JSONObject jsonError = json.getJSONObject("error");
        ServiceResponse result = new ServiceResponse(jsonError.getInt("code"));
        result.setMessage(jsonError.getString("info"));
        return result;
    }

    /**
     * Not supported.
     */
    @Deprecated
    @Override
    public ServiceResponse<List<Exchange>> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) {
        return null;
    }
}
