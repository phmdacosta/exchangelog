package com.pedrocosta.exchangelog.services.api;

import com.pedrocosta.exchangelog.request.api.FixerRequester;
import com.pedrocosta.exchangelog.models.Currency;
import com.pedrocosta.exchangelog.models.Exchange;
import com.pedrocosta.exchangelog.request.RestResponse;
import com.pedrocosta.exchangelog.services.BusinessService;
import com.pedrocosta.exchangelog.services.ServiceResponse;
import com.pedrocosta.exchangelog.utils.DateUtils;
import com.pedrocosta.exchangelog.utils.Log;
import com.pedrocosta.exchangelog.utils.MessageProperties;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Service to connect with Fixer webservice.
 *
 * @author Pedro H M da Costa
 */
@Service
public class FixerService implements BusinessService {

    private FixerRequester requester;
    private MessageProperties messageProperties;

    public FixerService(FixerRequester requester,
                        MessageProperties messageProperties) {
        this.requester = requester;
        this.messageProperties = messageProperties;
    }

    /**
     * Load a specific currency from Fixer API.
     *
     * @param code Currency code
     *
     * @return {@link ServiceResponse} object with {@link Currency}.
     * @throws JSONException
     */
    @Override
    public ServiceResponse<Currency> loadCurrency(String code) throws JSONException {
        ServiceResponse<Currency> result = new ServiceResponse<>(HttpStatus.OK);

        if (code != null) {
            RestResponse response = requester.symbols();

            if (response.isSuccess()) {
                JSONObject json = response.getJsonObject();
                Log.info(this, json.toString());
                
                JSONObject jsonCcy = json.getJSONObject("symbols");
                Iterator itCodes = jsonCcy.keys();

                while (itCodes.hasNext()) {
                    if (code.equals((String) itCodes.next())) {
                        result.setObject(new Currency(code, jsonCcy.getString(code)));
                        break;
                    }
                }

            } else {
                Log.error(this, getErrorMsg(response));
                result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
                result.setMessage(messageProperties.get())
                result.setMessage("Could not find currency " + code + " at Fixer"); //TODO use message properties
            }
        }

        return result;
    }

    /**
     * Load all currencies from Fixer API.
     *
     * @return {@link ServiceResponse} object with a list of {@link Currency}.
     * @throws JSONException
     */
    @Override
    public ServiceResponse<List<Currency>> loadCurrencies() throws JSONException {
        ServiceResponse<List<Currency>> result = new ServiceResponse<>(HttpStatus.OK);

        RestResponse response = requester.symbols();

        if (response.isSuccess()) {
            JSONObject json = response.getJsonObject();
            Log.info(this, json.toString());

            JSONObject jsonCcy = json.getJSONObject("symbols");
            Iterator itCodes = jsonCcy.keys();

            List<Currency> currencies = new ArrayList<>();

            while (itCodes.hasNext()) {
                String code = (String) itCodes.next();
                currencies.add(new Currency(code, jsonCcy.getString(code)));
            }

            result.setObject(currencies);

        } else {
            Log.error(this, getErrorMsg(response));
            result = new ServiceResponse<>(HttpStatus.NOT_FOUND);
            result.setMessage("Could not find any currency at Fixer"); //TODO use message properties
        }

        return result;
    }

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    public ServiceResponse<List<Exchange>> getQuoteRate(String baseCode) {
        return getQuoteRate(baseCode, null);
    }

    /**
     * Get quote rate between a base currency and others.
     *
     * @param baseCode      Base currency from what quote rate will be found.
     * @param quoteCodes    Quote currency array to what rate will be found.
     *
     * @return {@link ServiceResponse} object with a list of {@link Exchange}.
     */
    public ServiceResponse<List<Exchange>> getQuoteRate(String baseCode, String[] quoteCodes) {
        ServiceResponse<List<Exchange>> result = new ServiceResponse(HttpStatus.OK);

        try {
            String params;
            if (quoteCodes != null && quoteCodes.length > 0)
                params = "base="+baseCode+"&symbols="+quoteCodes;
            else
                params = "base="+baseCode;

            RestResponse response = requester.latest(params);

            JSONObject jsonExch = response.getJsonObject();
            List<Exchange> quotes = new ArrayList<>();

            if (response.isSuccess()) {

                Log.info(this, jsonExch.toString());

                Currency base = new Currency(baseCode, null);

                JSONObject jsonRates = jsonExch.getJSONObject("rates");
                Iterator itRates = jsonRates.keys();

                while (itRates.hasNext()) {
                    String quoteCode = (String) itRates.next();
                    if (quoteCode.equals(baseCode)) {
                        continue;
                    }

                    Currency quote = new Currency(quoteCode, null);
                    Date jsonDate = DateUtils.stringToDate(jsonExch.getString("date"), "yyyy-MM-dd");

                    double baseRate = jsonRates.getDouble(quoteCode);
                    quotes.add(createExchange(base, quote, baseRate, jsonDate));
                }

                result.setObject(quotes);
            } else {
                Log.error(this, getErrorMsg(response));
                result = new ServiceResponse(HttpStatus.NOT_FOUND);
                result.setMessage("Could not find any quote exchange at Fixer"); //TODO use message properties
            }
        } catch (JSONException e) {
            result = new ServiceResponse(HttpStatus.BAD_REQUEST);
        }

        return result;
    }

    /**
     * Amount and date definition are not supported by this version.
     */
    @Deprecated
    @Override
    public ServiceResponse<List<Exchange>> getQuoteRate(String baseCode, String[] quoteCodes, Double amount, Date valDate) {
        return getQuoteRate(baseCode, quoteCodes);
    }

    private Exchange createExchange(Currency ccy1, Currency ccy2, BigDecimal rate, Date valueDate) {
        Exchange exchange = new Exchange();
        exchange.setBaseCurrency(ccy1);
        exchange.setQuoteCurrency(ccy2);
        exchange.setValueDate(valueDate);
        exchange.setRate(rate);
        return exchange;
    }

    private Exchange createExchange(Currency ccy1, Currency ccy2, Double rate, Date valueDate) {
        return createExchange(ccy1, ccy2, BigDecimal.valueOf(rate), valueDate);
    }

    private String getErrorMsg(RestResponse response) throws JSONException {
        return response.getJsonObject().getJSONObject("error").getString("info");
    }
}
